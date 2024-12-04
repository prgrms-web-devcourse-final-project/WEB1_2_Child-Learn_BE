package com.prgrms.ijuju.domain.chat.dto.response;

import com.prgrms.ijuju.domain.chat.entity.Chat;

import java.time.LocalDateTime;
import java.time.Duration;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
        return ChatMessageResponseDTO.builder()
                .id(chat.getId())
                .senderId(chat.getSender().getId())
                .senderUsername(chat.getSender().getUsername())
                .senderProfileImage(chat.getSender().getProfileImage())
                .content(chat.getContent())
                .imageUrl(chat.getImageUrl())
                .createdAt(chat.getCreatedAt())
                .isRead(chat.isRead())
                .isDeleted(chat.isDeleted())
                .elapsedMinutes(Duration.between(chat.getCreatedAt(), LocalDateTime.now()).toMinutes())
                .build();
    }
}
