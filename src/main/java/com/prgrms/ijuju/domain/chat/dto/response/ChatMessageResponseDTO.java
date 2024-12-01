package com.prgrms.ijuju.domain.chat.dto.response;

import com.prgrms.ijuju.domain.chat.entity.Chat;

import java.time.LocalDateTime;
import java.time.Duration;

import lombok.Data;

@Data
public class ChatMessageResponseDTO {
    
    private Long id;
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
        ChatMessageResponseDTO dto = new ChatMessageResponseDTO();
        dto.setId(chat.getId());
        dto.setSenderId(chat.getSender().getId());
        dto.setSenderUsername(chat.getSender().getUsername());
        dto.setSenderProfileImage(chat.getSender().getProfileImage());
        dto.setContent(chat.getContent());
        dto.setImageUrl(chat.getImageUrl());
        dto.setCreatedAt(chat.getCreatedAt());
        dto.setRead(chat.isRead());
        dto.setDeleted(chat.isDeleted());
        dto.setElapsedMinutes(Duration.between(chat.getCreatedAt(), LocalDateTime.now()).toMinutes());
        return dto;
    }
}
