package com.prgrms.ijuju.domain.chat.dto.response;

import com.prgrms.ijuju.domain.chat.entity.Chat;

import java.time.LocalDateTime;
import java.time.Duration;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponseDTO {
    
    private String id;
    private Long senderId;
    private String senderUsername;
    private String senderProfileImage;
    private String content;
    private String imageUrl;
    private LocalDateTime createdAt;
    private boolean isRead;
    private boolean isDeleted;
    private Long elapsedMinutes;
    
    public static ChatMessageResponseDTO from(Chat chat) {
        return ChatMessageResponseDTO.builder()
                .id(chat.getId())
                .senderId(chat.getSenderId())
                .senderUsername(chat.getSenderUsername())
                .senderProfileImage(chat.getSenderProfileImage())
                .content(chat.getContent())
                .imageUrl(chat.getImageUrl())
                .createdAt(chat.getCreatedAt())
                .isRead(chat.isRead())
                .isDeleted(chat.isDeleted())
                .elapsedMinutes(Duration.between(chat.getCreatedAt(), LocalDateTime.now()).toMinutes())
                .build();
    }
}
