package com.prgrms.ijuju.domain.chat.service;

import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ijuju.domain.chat.dto.request.ChatMessageRequestDTO;
import com.prgrms.ijuju.domain.chat.dto.response.ChatMessageResponseDTO;
import com.prgrms.ijuju.domain.chat.dto.response.ChatRoomListResponseDTO;
import com.prgrms.ijuju.domain.chat.entity.Chat;
import com.prgrms.ijuju.domain.chat.entity.ChatRoom;
import com.prgrms.ijuju.domain.chat.exception.ChatException;
import com.prgrms.ijuju.domain.chat.repository.ChatRepository;
import com.prgrms.ijuju.domain.chat.repository.ChatRoomRepository;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.global.exception.CustomException;
import com.prgrms.ijuju.domain.member.entity.Member;

import java.util.List;
import java.util.Comparator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {
    
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;

    // 채팅방 목록 조회
    public List<ChatRoomListResponseDTO> getChatRoomList(Long userId) {
        return chatRoomRepository.findByMemberIdOrFriendId(userId, userId).stream()
                .filter(room -> !room.isDeleted())
                .map(room -> ChatRoomListResponseDTO.from(room, userId))
                .sorted(Comparator.comparing(
                    dto -> dto.getLastMessageTime(), 
                    Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    // 채팅방 생성 또는 복구
    public ChatRoom createOrRestoreChatRoom(Long memberId, Long friendId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ChatException.USER_NOT_FOUND));
        Member friend = memberRepository.findById(friendId)
                .orElseThrow(() -> new CustomException(ChatException.USER_NOT_FOUND));

        List<ChatRoom> existingRooms = chatRoomRepository
                .findByMemberIdOrFriendId(memberId, friendId);

        if (!existingRooms.isEmpty()) {
            ChatRoom room = existingRooms.get(0);
            if (room.isDeleted()) {
                room.restore();
            }
            return room;
        }

        return chatRoomRepository.save(ChatRoom.builder()
                .member(member)
                .friend(friend)
                .build());
    }

    // 채팅방 삭제
    public void deleteChatRoom(Long roomId, Long userId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ChatException.CHATROOM_NOT_FOUND));

        if (!room.getMember().getId().equals(userId) && 
            !room.getFriend().getId().equals(userId)) {
            throw new CustomException(ChatException.UNAUTHORIZED_CHATROOM_ACCESS);
        }

        room.markAsDeleted();
    }

    // 채팅방 메시지 조회
    public List<ChatMessageResponseDTO> getMessagesByChatRoomId(Long roomId, Long userId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ChatException.CHATROOM_NOT_FOUND));
                
        List<Chat> messages = chatRepository.findByChatRoomIdOrderByCreatedAtAsc(roomId);
        messages.stream()
                .filter(chat -> !chat.getSender().getId().equals(userId))
                .forEach(Chat::markAsRead);
                
        return messages.stream()
                .map(ChatMessageResponseDTO::from)
                .collect(Collectors.toList());
    }

    // 메시지 전송
    public ChatMessageResponseDTO sendMessage(ChatMessageRequestDTO request, Long senderId) {
        ChatRoom room = chatRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new CustomException(ChatException.CHATROOM_NOT_FOUND));
        
        Member sender = memberRepository.findById(senderId)
                .orElseThrow(() -> new CustomException(ChatException.USER_NOT_FOUND));

        Chat chat = chatRepository.save(Chat.builder()
                .chatRoom(room)
                .sender(sender)
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .build());

        return ChatMessageResponseDTO.from(chat);
    }

    // 메시지 읽음 처리
    public void markAsRead(Long roomId, Long userId) {
        List<Chat> unreadChats = chatRepository.findByChatRoomIdAndIsReadFalseAndSenderIdNot(roomId, userId);
        unreadChats.forEach(Chat::markAsRead);
    }

    // 메시지 삭제
    public void deleteMessage(Long messageId, Long userId) {
        Chat chat = chatRepository.findById(messageId)
                .orElseThrow(() -> new CustomException(ChatException.MESSAGE_NOT_FOUND));

        if (!chat.getSender().getId().equals(userId)) {
            throw new CustomException(ChatException.UNAUTHORIZED_MESSAGE_DELETION);
        }

        chat.delete();
    }
}
