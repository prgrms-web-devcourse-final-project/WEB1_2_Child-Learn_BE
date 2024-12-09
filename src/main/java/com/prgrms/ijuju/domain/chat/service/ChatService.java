package com.prgrms.ijuju.domain.chat.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.prgrms.ijuju.domain.chat.dto.response.ChatMessageResponseDTO;
import com.prgrms.ijuju.domain.chat.dto.response.ChatRoomListResponseDTO;
import com.prgrms.ijuju.domain.chat.dto.response.ChatReadResponseDTO;
import com.prgrms.ijuju.domain.chat.repository.ChatRepository;
import com.prgrms.ijuju.domain.chat.entity.Chat;
import com.prgrms.ijuju.domain.chat.entity.ChatRoom;
import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.chat.exception.ChatException;
import com.prgrms.ijuju.domain.chat.exception.ChatErrorCode;
import com.prgrms.ijuju.domain.chat.repository.ChatRoomRepository;
import com.prgrms.ijuju.domain.chat.repository.ChatMessageRepository;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.friend.repository.FriendListRepository;
import com.prgrms.ijuju.domain.avatar.service.FileStorageService;

import java.util.stream.Collectors;
import java.util.List;
import java.time.LocalDateTime;
import java.io.IOException;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;
    private final ChatRepository chatRepository;
    private final ChatCacheService chatCacheService;
    private final FileStorageService fileStorageService;
    private final FriendListRepository friendListRepository;

    private static final int PAGE_SIZE = 20;

    // 채팅 저장
    public Chat saveChat(Chat chat) {
        log.info("Saving chat: {}", chat);
        Chat savedChat = chatRepository.save(chat);
        log.info("Saved chat: {}", savedChat);
        return savedChat;
    }
    
    // 채팅방 생성 또는 조회
    @Transactional
    public ChatRoom createChatRoom(Long memberId, Long friendId) {
        // 친구 관계 확인
        if (!friendListRepository.existsByMemberIdAndFriendId(memberId, friendId)) {
            throw new ChatException(ChatErrorCode.NOT_FRIENDS);
        }

        // 이미 존재하는 채팅방 확인
        Optional<ChatRoom> existingRoom = chatRoomRepository.findByMemberIdAndFriendId(memberId, friendId);
        if (existingRoom.isPresent()) {
            return existingRoom.get();
        }

        // 새 채팅방 생성
        ChatRoom chatRoom = ChatRoom.builder()
            .memberId(memberId)
            .friendId(friendId)
            .build();

        return chatRoomRepository.save(chatRoom);
    }

    // 채팅방 삭제 (논리적 삭제)
    @Transactional
    public void deleteChatRoom(String roomId, Long userId) {
        // 채팅방이 존재하는지 먼저 확인
        if (!chatRoomRepository.existsById(roomId)) {
            throw new ChatException(ChatErrorCode.CHATROOM_NOT_FOUND);
        }

        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
            .orElseThrow(() -> new ChatException(ChatErrorCode.CHATROOM_NOT_FOUND));

        // 이미 삭제된 채팅방인지 확인
        if (chatRoom.isDeleted() && chatRoom.getDeletedByUsers().contains(userId)) {
            throw new ChatException(ChatErrorCode.CHATROOM_ALREADY_DELETED);
        }

        if (!chatRoom.getMemberId().equals(userId) && !chatRoom.getFriendId().equals(userId)) {
            throw new ChatException(ChatErrorCode.CHATROOM_ACCESS_DENIED);
        }

        chatRoom.markAsDeleted(userId);
        
        if (chatRoom.shouldBeCompletelyDeleted()) {
            // 채팅방과 관련된 모든 메시지 삭제
            chatMessageRepository.deleteByRoomId(roomId);
            // 채팅방 삭제
            chatRoomRepository.delete(chatRoom);
            // 캐시 삭제
            chatCacheService.invalidateCache(roomId);
        } else {
            chatRoomRepository.save(chatRoom);
        }
    }

    // 채팅방 목록 조회
    @Transactional(readOnly = true)
    public List<ChatRoomListResponseDTO> showChatRooms(Long userId) {
        log.info("사용자 {}의 채팅방 목록 조회 시작", userId);
        
        List<ChatRoom> rooms = chatRoomRepository.findByMemberIdOrFriendId(userId, userId);
        
        return rooms.stream()
            .filter(room -> {
                return !room.isDeleted() || 
                       (room.isDeleted() && !room.getDeletedByUsers().contains(userId));
            })
            .map(room -> {
                Member friend = memberRepository.findById(
                    room.getMemberId().equals(userId) ? room.getFriendId() : room.getMemberId()
                ).orElseThrow(() -> new ChatException(ChatErrorCode.MEMBER_NOT_FOUND));
                return ChatRoomListResponseDTO.from(room, friend, userId);
            })
            .collect(Collectors.toList());
    }

    // 채팅 메시지 조회
    public Page<ChatMessageResponseDTO> showChatMessages(String roomId, Long userId, int page) {
        // 캐시된 메시지 확인
        List<ChatMessageResponseDTO> cachedMessages = chatCacheService.getRecentMessages(roomId);
        if (cachedMessages != null && page == 0) {
            return new PageImpl<>(cachedMessages);
        }

        // DB에서 페이징 조회
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("createdAt").descending());
        Page<Chat> messages = chatMessageRepository.findByRoomIdOrderByCreatedAtDesc(roomId, pageable);
        
        // 첫 페이지인 경우 캐시 업데이트
        if (page == 0) {
            List<ChatMessageResponseDTO> messageDTOs = messages.getContent().stream()
                .map(ChatMessageResponseDTO::from)
                .collect(Collectors.toList());
            chatCacheService.cacheRecentMessages(roomId, messageDTOs, 24 * 60); // 24시간 캐시
        }

        return messages.map(ChatMessageResponseDTO::from);
    }

    // 메시지 전송
    public ChatMessageResponseDTO sendMessage(String roomId, Long senderId, String content, MultipartFile image) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
            .orElseThrow(() -> new ChatException(ChatErrorCode.CHATROOM_NOT_FOUND));

        // 친구 관계 확인

        validateMessage(content, image);
        validateChatRoomAccess(chatRoom, senderId);

        Member sender = memberRepository.findById(senderId)
            .orElseThrow(() -> new ChatException(ChatErrorCode.MEMBER_NOT_FOUND));

        String imageUrl = null;

        if (image != null && !image.isEmpty()) {
            try {
                imageUrl = fileStorageService.storeFile(image);
            } catch (IOException e) {
                throw new ChatException(ChatErrorCode.FILE_UPLOAD_ERROR);
            }
        }

        // 추후 메시지 암호화 추가하기
        Chat chat = Chat.builder()
            .roomId(chatRoom.getId())
            .senderLoginId(sender.getLoginId())
            .senderUsername(sender.getUsername())
            .senderProfileImage(sender.getProfileImage())
            .content(content)
            .imageUrl(imageUrl)
            .createdAt(LocalDateTime.now())
            .isRead(false)
            .isDeleted(false)
            .build();
        
        chatMessageRepository.save(chat);
        
        chatCacheService.invalidateCache(roomId);
        
        return ChatMessageResponseDTO.from(chat);
    }

    // 메시지 유효성 검사
    private void validateMessage(String content, MultipartFile image) {
        if ((content == null || content.trim().isEmpty()) && image == null) {
            throw new ChatException(ChatErrorCode.MESSAGE_CONTENT_EMPTY);
        }
        if (content != null && content.length() > 1000) {
            throw new ChatException(ChatErrorCode.MESSAGE_TOO_LONG);
        }
    }

    // 메시지 삭제
    public void deleteMessage(String messageId, Long userId) {
        Chat chat = chatMessageRepository.findById(messageId)
            .orElseThrow(() -> new ChatException(ChatErrorCode.MESSAGE_NOT_FOUND));

        if (!chat.canDelete(userId)) {
            throw new ChatException(ChatErrorCode.MESSAGE_DELETION_TIMEOUT);
        }

        chat.delete();
        chatMessageRepository.save(chat);
    }

    // 읽지 않은 메시지 수 조회
    public int showUnreadCount(Long userId) {
        List<ChatRoom> rooms = chatRoomRepository.findByMemberIdOrFriendId(userId, userId);
        
        return rooms.stream()
            .filter(room -> !room.isDeleted())
            .mapToInt(room -> room.getUnreadCount(userId))
            .sum();
    }

    // 메시지 읽음 처리
    public ChatReadResponseDTO markMessagesAsRead(String roomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
            .orElseThrow(() -> new ChatException(ChatErrorCode.CHATROOM_NOT_FOUND));

        chatRoom.markMessagesAsRead(userId);
        chatRoomRepository.save(chatRoom);

        return ChatReadResponseDTO.builder()
            .userId(userId)
            .roomId(roomId)
            .readAt(LocalDateTime.now())
            .build();
    }

    // 스크롤 메시지 조회
    public Page<ChatMessageResponseDTO> showMessagesByScroll(String roomId, String lastMessageId, int size, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
            .orElseThrow(() -> new ChatException(ChatErrorCode.CHATROOM_NOT_FOUND));
        validateChatRoomAccess(chatRoom, userId);
        
        Pageable pageable = PageRequest.of(0, size, Sort.by("createdAt").descending());
        
        // 캐시 우선 조회
        List<ChatMessageResponseDTO> cachedMessages = chatCacheService.getRecentMessages(roomId);
        if (cachedMessages != null) {
            return new PageImpl<>(cachedMessages, pageable, cachedMessages.size());
        }

        // DB 조회
        Page<Chat> messages;
        if (lastMessageId != null) {
            Chat lastMessage = chatMessageRepository.findById(lastMessageId)
                .orElseThrow(() -> new ChatException(ChatErrorCode.MESSAGE_NOT_FOUND));
            messages = chatMessageRepository
                .findByRoomIdAndCreatedAtBeforeOrderByCreatedAtDesc(roomId, lastMessage.getCreatedAt(), pageable);
        } else {
            messages = chatMessageRepository.findByRoomIdOrderByCreatedAtDesc(roomId, pageable);
        }

        List<ChatMessageResponseDTO> messageDTOs = messages.getContent().stream()
            .map(ChatMessageResponseDTO::from)
            .collect(Collectors.toList());

        return new PageImpl<>(messageDTOs, pageable, messages.getTotalElements());
    }

    // 초기 로딩시 캐시 저장
    public List<ChatMessageResponseDTO> showMessages(String roomId, String lastMessageId, int size) {
        // 캐시 확에 페이지 정보 포함
        String cacheKey = String.format("%s:%s:%d", roomId, lastMessageId, size);
        
        // 캐시 확인
        List<ChatMessageResponseDTO> cachedMessages = chatCacheService.getRecentMessages(cacheKey);
        if (cachedMessages != null) {
            return cachedMessages;
        }

        // 커서 기반 페이징으로 변경
        Pageable pageable = PageRequest.of(0, size);
        Page<Chat> messages = lastMessageId == null ?
                chatMessageRepository.findByRoomIdOrderByCreatedAtDesc(roomId, pageable) :
                chatMessageRepository.findByRoomIdAndIdLessThanOrderByCreatedAtDesc(roomId, lastMessageId, pageable);

        List<ChatMessageResponseDTO> messageDTOs = messages.stream()
                .map(ChatMessageResponseDTO::from)
                .collect(Collectors.toList());

        // 캐시 저장 (짧은 TTL 설정)
        chatCacheService.cacheRecentMessages(cacheKey, messageDTOs, 5); // 5분 캐시

        return messageDTOs;
    }

    // 채팅방 접근 검증
    private void validateChatRoomAccess(ChatRoom chatRoom, Long userId) {
        if (!chatRoom.getMemberId().equals(userId) && !chatRoom.getFriendId().equals(userId)) {
            throw new ChatException(ChatErrorCode.CHATROOM_ACCESS_DENIED);
        }
        if (chatRoom.isDeleted()) {
            throw new ChatException(ChatErrorCode.CHATROOM_DELETED);
        }
    }
}
