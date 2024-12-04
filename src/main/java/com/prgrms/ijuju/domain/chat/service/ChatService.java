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
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {
    
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final ChatRepository chatRepository;

    // 채팅방 목록 조회
    @Transactional(readOnly = true)
    public List<ChatRoomListResponseDTO> getChatRoomList(Long userId) {
        return chatRoomRepository.findByMemberIdOrFriendId(userId, userId).stream()
            .filter(chatRoom -> !chatRoom.isDeleted() && 
                chatRoom.getMember().getId().equals(userId))
            .map(chatRoom -> ChatRoomListResponseDTO.from(chatRoom, userId))
            .sorted(Comparator.comparing(ChatRoomListResponseDTO::getLastMessageTime, 
                Comparator.nullsLast(Comparator.reverseOrder())))
            .collect(Collectors.toList());
    }

    @Transactional
    public ChatRoom createChatRoom(Long memberId, Long friendId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ChatException.USER_NOT_FOUND));
        Member friend = memberRepository.findById(friendId)
            .orElseThrow(() -> new CustomException(ChatException.USER_NOT_FOUND));
            
        // 내 채팅방만 확인 (삭제된 경우 새로 생성)
        Optional<ChatRoom> existingChatRoom = chatRoomRepository.findByMemberIdAndFriendIdAndIsDeletedFalse(memberId, friendId);
        
        if (existingChatRoom.isPresent()) {
            return existingChatRoom.get();
        }
    
        // 새로운 채팅방 생성
        ChatRoom newChatRoom = chatRoomRepository.save(ChatRoom.builder()
            .member(member)
            .friend(friend)
            .build());
        
        return newChatRoom;
    }

    // 특정 채팅방의 메시지 조회
    @Transactional(readOnly = true)
    public List<ChatMessageResponseDTO> getMessagesByChatRoomId(Long roomId, Long userId) {
        // 채팅방이 존재하는지 확인
        chatRoomRepository.findById(roomId)
            .orElseThrow(() -> new CustomException(ChatException.CHATROOM_NOT_FOUND));
        
        // 채팅방의 메시지 목록을 조회
        return chatRepository.findByChatRoomIdOrderByCreatedAtDesc(roomId).stream()
            .map(ChatMessageResponseDTO::from)
            .collect(Collectors.toList());
    }
    
    // 채팅방 삭제
    @Transactional
    public void deleteChatRoom(Long roomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
            .orElseThrow(() -> new CustomException(ChatException.CHATROOM_NOT_FOUND));
        
        if (!chatRoom.getMember().getId().equals(userId) && 
            !chatRoom.getFriend().getId().equals(userId)) {
            throw new CustomException(ChatException.UNAUTHORIZED_CHATROOM_ACCESS);
        }
        
        // 삭제 처리
        chatRoom.delete();
        chatRoom.resetUnreadCount();
    }

    // 메시지 저장
    @Transactional
    public ChatMessageResponseDTO saveMessage(ChatMessageRequestDTO request, Long senderId) {
        ChatRoom senderChatRoom = chatRoomRepository.findById(request.getRoomId())
            .orElseThrow(() -> new CustomException(ChatException.CHATROOM_NOT_FOUND));
        
        // 발신자와 수신자 확인
        Member sender = memberRepository.findById(senderId)
            .orElseThrow(() -> new CustomException(ChatException.USER_NOT_FOUND));
        Member receiver = senderChatRoom.getMember().getId().equals(senderId) 
            ? senderChatRoom.getFriend() 
            : senderChatRoom.getMember();

        // 수신자의 채팅방 찾기
        ChatRoom receiverChatRoom = chatRoomRepository
            .findByMemberIdAndFriendIdAndIsDeletedFalse(receiver.getId(), sender.getId())
            .orElseGet(() -> createChatRoom(receiver.getId(), sender.getId()));

        // 읽지 않은 메시지 수 증가
        receiverChatRoom.incrementUnreadCount();
        
        // 발신자 채팅방에 메시지 저장
        Chat senderChat = Chat.builder()
            .chatRoom(senderChatRoom)
            .sender(sender)
            .content(request.getContent())
            .imageUrl(request.getImageUrl())
            .build();
        
        // 수신자 채팅방에 메시지 저장
        Chat receiverChat = Chat.builder()
            .chatRoom(receiverChatRoom)
            .sender(sender)
            .content(request.getContent())
            .imageUrl(request.getImageUrl())
            .build();

        // 양쪽 채팅방 모두 저장
        Chat savedSenderChat = chatRepository.save(senderChat);
        chatRepository.save(receiverChat);

        // 마지막 메시지 업데이트
        senderChatRoom.updateLastMessage(savedSenderChat);
        receiverChatRoom.updateLastMessage(receiverChat);

        return ChatMessageResponseDTO.from(savedSenderChat);
    }

    // 메시지 읽음 처리
    public void markAsRead(Long roomId, Long userId) {
        List<Chat> unreadMessages = chatRepository
            .findByChatRoomIdAndSenderIdNotAndIsReadFalseAndIsDeletedFalse(roomId, userId);
            
        unreadMessages.forEach(Chat::markAsRead);
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

    // 읽지 않은 메시지 개수 조회
    public int getUnreadMessageCount(Long roomId, Long userId) {
        return chatRepository.countByChatRoomIdAndSenderIdNotAndIsReadFalseAndIsDeletedFalse(roomId, userId);
    }

}
