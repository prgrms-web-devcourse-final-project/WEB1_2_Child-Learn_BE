package com.prgrms.ijuju.domain.chat.dto.response;

import java.time.LocalDateTime;
import java.time.Duration;

import com.prgrms.ijuju.domain.chat.entity.ChatRoom;
import com.prgrms.ijuju.domain.chat.entity.Chat;
import com.prgrms.ijuju.domain.member.entity.Member;

import lombok.Getter;
import lombok.Builder;

@Getter
@Builder
public class ChatRoomListResponseDTO {
    
    private final String roomId;
    private final Long friendId;
    private final String friendUsername;
    private final String friendLoginId;
    private final String friendProfileImage;
    private final boolean friendIsActive;
    private final String lastMessage;
    private final LocalDateTime lastMessageTime;
    private final int unreadCount;
    private final Long elapsedMinutes;
    
    public static ChatRoomListResponseDTO from(ChatRoom chatRoom, Member friend, Long userId) {
        Chat lastChat = chatRoom.getLastMessage();
        
        return ChatRoomListResponseDTO.builder()
            .roomId(chatRoom.getId())
            .friendId(friend.getId())
            .friendUsername(friend.getUsername())
            .friendLoginId(friend.getLoginId())
            .friendProfileImage(friend.getProfileImage())
            .friendIsActive(friend.isActive())
            .lastMessage(lastChat != null ? lastChat.getContent() : "메시지가 없습니다.")
            .lastMessageTime(lastChat != null ? lastChat.getCreatedAt() : null)
            .unreadCount(chatRoom.getUnreadCount(userId))
            .elapsedMinutes(lastChat != null ? 
                Duration.between(lastChat.getCreatedAt(), LocalDateTime.now()).toMinutes() : null)
            .build();
    }
} 
