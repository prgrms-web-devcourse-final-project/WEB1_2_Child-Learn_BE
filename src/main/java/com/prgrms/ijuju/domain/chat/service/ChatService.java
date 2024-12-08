package com.prgrms.ijuju.domain.chat.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.redis.core.RedisTemplate;
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
import com.prgrms.ijuju.domain.avatar.service.FileStorageService;

import java.util.stream.Collectors;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatRepository chatRepository;
    private final ChatCacheService chatCacheService;
    private final FileStorageService fileStorageService;
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
    public ChatRoomListResponseDTO createChatRoom(Long memberId, Long friendId) {
        // 자기 자신과의 채팅 방지
        if (memberId.equals(friendId)) {
            throw new ChatException(ChatErrorCode.USER_SELF_CHAT);
        }

        // 사용자 존재 여부 확인
        memberRepository.findById(memberId)
            .orElseThrow(() -> new ChatException(ChatErrorCode.MEMBER_NOT_FOUND));
        Member friend = memberRepository.findById(friendId)
            .orElseThrow(() -> new ChatException(ChatErrorCode.MEMBER_NOT_FOUND));

        // 중복 채팅방 검사 (양방향 체크)
        if (chatRoomRepository.existsByMemberIdAndFriendId(memberId, friendId) ||
            chatRoomRepository.existsByMemberIdAndFriendId(friendId, memberId)) {
            throw new ChatException(ChatErrorCode.CHATROOM_ALREADY_EXISTS);
        }

        // 새로운 채팅방 생성
        ChatRoom chatRoom = ChatRoom.builder()
            .memberId(memberId)
            .friendId(friendId)
            .build();
        
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        return ChatRoomListResponseDTO.from(savedChatRoom, friend, memberId);
    }

    // 채팅방 삭제 (논리적 삭제)
    public void deleteChatRoom(String roomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
            .orElseThrow(() -> new ChatException(ChatErrorCode.CHATROOM_NOT_FOUND));

        if (!chatRoom.getMemberId().equals(userId) && !chatRoom.getFriendId().equals(userId)) {
            throw new ChatException(ChatErrorCode.CHATROOM_ACCESS_DENIED);
        }

        chatRoom.markAsDeleted();
        chatRoomRepository.save(chatRoom);
    }

    // 채팅방 목록 조회
    @Transactional(readOnly = true)
    public List<ChatRoomListResponseDTO> showChatRooms(Long userId) {
        log.info("사용자 {}의 채팅방 목록 조회 시작", userId);
        
        List<ChatRoom> rooms = chatRoomRepository.findByMemberIdOrFriendId(userId, userId);
        
        if (rooms.isEmpty()) {
            log.info("사용자 {}의 채팅방이 없습니다.", userId);
            return Collections.emptyList();
        }

        // 친구 ID 목록 추출
        List<Long> friendIds = rooms.stream()
                .map(room -> room.getMemberId().equals(userId) ? 
                     room.getFriendId() : room.getMemberId())
                .collect(Collectors.toList());

        // 친구 정보 한 번에 조회
        List<Member> friends = memberRepository.findAllById(friendIds);
        Map<Long, Member> friendMap = friends.stream()
                .collect(Collectors.toMap(Member::getId, f -> f));

        // DTO 변환
        List<ChatRoomListResponseDTO> result = rooms.stream()
                .map(room -> {
                    Long friendId = room.getMemberId().equals(userId) ? 
                                  room.getFriendId() : room.getMemberId();
                    Member friend = friendMap.get(friendId);
                    return ChatRoomListResponseDTO.from(room, friend, userId);
                })
                .collect(Collectors.toList());

        log.info("사용자 {}의 채팅방 {}개 조회 완료", userId, result.size());
        return result;
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
            chatCacheService.cacheRecentMessages(roomId, messageDTOs);
        }

        return messages.map(ChatMessageResponseDTO::from);
    }

    // 메시지 전송
    public ChatMessageResponseDTO sendMessage(String roomId, Long senderId, String content, MultipartFile image) {
        validateMessage(content, image);
        
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
            .orElseThrow(() -> new ChatException(ChatErrorCode.CHATROOM_NOT_FOUND));
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

        Chat chat = Chat.builder()
            .roomId(chatRoom.getId())
            .senderId(sender.getId())
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
        // 캐시 확인 (초기 로딩시)
        if (lastMessageId == null) {
            List<ChatMessageResponseDTO> cachedMessages = chatCacheService.getRecentMessages(roomId);
            if (cachedMessages != null && !cachedMessages.isEmpty()) {
                return cachedMessages;
            }
        }
    
        Pageable pageable = PageRequest.of(0, size, Sort.by("createdAt").descending());
        Page<Chat> messages = chatMessageRepository.findByRoomIdOrderByCreatedAtDesc(roomId, pageable);
        
        List<ChatMessageResponseDTO> messageDTOs = messages.stream()
            .map(ChatMessageResponseDTO::from)
            .collect(Collectors.toList());
    
        // 초기 로딩시 캐시 저장
        if (lastMessageId == null) {
            chatCacheService.cacheRecentMessages(roomId, messageDTOs);
        }
    
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
