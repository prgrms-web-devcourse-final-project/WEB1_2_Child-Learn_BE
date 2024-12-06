package com.prgrms.ijuju.domain.chat.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.redis.core.RedisTemplate;

import com.prgrms.ijuju.domain.chat.dto.response.ChatMessageResponseDTO;
import com.prgrms.ijuju.domain.chat.dto.response.ChatRoomListResponseDTO;
import com.prgrms.ijuju.domain.chat.dto.response.ChatReadResponseDTO;
import com.prgrms.ijuju.domain.chat.repository.ChatRepository;
import com.prgrms.ijuju.domain.chat.entity.Chat;
import com.prgrms.ijuju.domain.chat.entity.ChatRoom;
import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.chat.exception.ChatException;
import com.prgrms.ijuju.domain.chat.repository.ChatRoomRepository;
import com.prgrms.ijuju.domain.chat.repository.ChatMessageRepository;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;

import java.util.stream.Collectors;
import java.util.List;
import java.util.Comparator;
import java.time.LocalDateTime;
import java.util.ArrayList;

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

    // 채팅 저장
    public Chat saveChat(Chat chat) {
        log.info("Saving chat: {}", chat);
        Chat savedChat = chatRepository.save(chat);
        log.info("Saved chat: {}", savedChat);
        return savedChat;
    }

    // 채팅방 생성 또는 조회
    public ChatRoomListResponseDTO createOrGetChatRoom(Long memberId, Long friendId) {
        if (memberId.equals(friendId)) {
            throw ChatException.USER_SELF_CHAT.toException();
        }

        memberRepository.findById(memberId)
            .orElseThrow(() -> ChatException.MEMBER_NOT_FOUND.toException());
        Member friend = memberRepository.findById(friendId)
            .orElseThrow(() -> ChatException.MEMBER_NOT_FOUND.toException());

        ChatRoom chatRoom = chatRoomRepository
            .findByMemberIdAndFriendIdAndIsDeletedFalse(memberId, friendId)
            .orElseGet(() -> {
                ChatRoom newRoom = ChatRoom.builder()
                    .memberId(memberId)
                    .friendId(friendId)
                    .build();
                return chatRoomRepository.save(newRoom);
            });

        return ChatRoomListResponseDTO.from(chatRoom, friend, memberId);
    }

    // 채팅방 삭제 (논리적 삭제)
    public void deleteChatRoom(String roomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
            .orElseThrow(() -> ChatException.CHATROOM_NOT_FOUND.toException());

        if (!chatRoom.getMemberId().equals(userId) && !chatRoom.getFriendId().equals(userId)) {
            throw ChatException.CHATROOM_ACCESS_DENIED.toException();
        }

        chatRoom.markAsDeleted();
        chatRoomRepository.save(chatRoom);
    }

    // 채팅방 목록 조회
    public List<ChatRoomListResponseDTO> getChatRooms(Long userId) {
        List<ChatRoom> rooms = chatRoomRepository.findByMemberIdOrFriendId(userId, userId);
        
        return rooms.stream()
            .filter(room -> !room.isDeleted())
            .map(room -> {
                Member friend = memberRepository.findById(
                    room.getMemberId().equals(userId) ? room.getFriendId() : room.getMemberId()
                ).orElseThrow(() -> ChatException.MEMBER_NOT_FOUND.toException());
                
                room.getChat();
                
                return ChatRoomListResponseDTO.from(room, friend, userId);
            })
            .sorted(Comparator.comparing(ChatRoomListResponseDTO::getLastMessageTime, 
                Comparator.nullsLast(Comparator.reverseOrder())))
            .collect(Collectors.toList());
    }

    // 채팅 메시지 조회
    public List<ChatMessageResponseDTO> getChatMessages(String roomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
            .orElseThrow(() -> ChatException.CHATROOM_NOT_FOUND.toException());

        if (!chatRoom.getMemberId().equals(userId) && !chatRoom.getFriendId().equals(userId)) {
            throw ChatException.CHATROOM_ACCESS_DENIED.toException();
        }

        List<Chat> chats = chatRoom.getChat();
        if (chats == null || chats.isEmpty()) {
            return new ArrayList<>();
        }

        // 메시지 읽음 처리
        chatRoom.markMessagesAsRead(userId);
        chatRoomRepository.save(chatRoom);

        return chats.stream()
            .filter(chat -> !chat.isDeleted())
            .map(chat -> ChatMessageResponseDTO.from(chat))
            .collect(Collectors.toList());
    }

    // 메시지 전송
    public ChatMessageResponseDTO sendMessage(String roomId, Long senderId, String content, MultipartFile image) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
            .orElseThrow(() -> ChatException.CHATROOM_NOT_FOUND.toException());

        Member sender = memberRepository.findById(senderId)
            .orElseThrow(() -> ChatException.MEMBER_NOT_FOUND.toException());

        Chat chat = Chat.createChatMessage(chatRoom, sender, content, image);
        chat = chatMessageRepository.save(chat);
        chatRoom.addChat(chat);
        chatRoomRepository.save(chatRoom);

        // Redis를 통한 실시간 메시지 발행
        redisTemplate.convertAndSend("chat." + roomId, ChatMessageResponseDTO.from(chat));

        return ChatMessageResponseDTO.from(chat);
    }

    // 메시지 삭제
    public void deleteMessage(String messageId, Long userId) {
        Chat chat = chatMessageRepository.findById(messageId)
            .orElseThrow(() -> ChatException.MESSAGE_NOT_FOUND.toException());

        if (!chat.canDelete(userId)) {
            throw ChatException.MESSAGE_DELETION_TIMEOUT.toException();
        }

        chat.delete();
        chatMessageRepository.save(chat);
    }

    // 읽지 않은 메시지 수 조회
    public int getUnreadCount(Long userId) {
        List<ChatRoom> rooms = chatRoomRepository.findByMemberIdOrFriendId(userId, userId);
        
        return rooms.stream()
            .filter(room -> !room.isDeleted())
            .mapToInt(room -> room.getUnreadCount(userId))
            .sum();
    }

    // 메시지 읽음 처리
    public ChatReadResponseDTO markMessagesAsRead(String roomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
            .orElseThrow(() -> ChatException.CHATROOM_NOT_FOUND.toException());

        chatRoom.markMessagesAsRead(userId);
        chatRoomRepository.save(chatRoom);

        return ChatReadResponseDTO.builder()
            .userId(userId)
            .roomId(roomId)
            .readAt(LocalDateTime.now())
            .build();
    }
}
