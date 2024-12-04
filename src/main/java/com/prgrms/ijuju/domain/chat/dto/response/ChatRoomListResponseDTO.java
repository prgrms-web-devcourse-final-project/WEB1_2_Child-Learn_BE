package com.prgrms.ijuju.domain.chat.dto.response;

import java.time.LocalDateTime;

import com.prgrms.ijuju.domain.chat.entity.ChatRoom;
import com.prgrms.ijuju.domain.chat.entity.Chat;
import com.prgrms.ijuju.domain.member.entity.Member;

import lombok.Getter;

@Getter
public class ChatRoomListResponseDTO {
    
    private final Long roomId;
    private final Long friendId;
    private final String friendUsername;
    private final String friendProfileImage;
    private final boolean friendIsActive;
    private final String lastMessage;
    private final LocalDateTime lastMessageTime;
    private final int unreadCount;
    
    private ChatRoomListResponseDTO(Long roomId, Long friendId, String friendUsername, 
            String friendProfileImage, boolean friendIsActive, String lastMessage, 
            LocalDateTime lastMessageTime, int unreadCount) {
        this.roomId = roomId;
        this.friendId = friendId;
        this.friendUsername = friendUsername;
        this.friendProfileImage = friendProfileImage;
        this.friendIsActive = friendIsActive;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
        this.unreadCount = unreadCount;
    }
    
    public static ChatRoomListResponseDTO from(ChatRoom chatRoom, Long userId) {
        Member friend = chatRoom.getMember().getId().equals(userId) 
            ? chatRoom.getFriend() 
            : chatRoom.getMember();
            
        Chat lastChat = chatRoom.getLastMessage();
        String lastMessageContent = lastChat != null ? lastChat.getContent() : "메시지가 없습니다.";
        LocalDateTime lastMessageTime = lastChat != null ? lastChat.getCreatedAt() : null;

        return new ChatRoomListResponseDTO(
            chatRoom.getId(),
            friend.getId(),
            friend.getUsername(),
            friend.getProfileImage(),
            friend.isActive(),
            lastMessageContent,
            lastMessageTime,
            chatRoom.getUnreadCount(userId)
        );
    }
} 