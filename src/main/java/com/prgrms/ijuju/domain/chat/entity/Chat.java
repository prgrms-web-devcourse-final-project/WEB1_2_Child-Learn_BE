package com.prgrms.ijuju.domain.chat.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.prgrms.ijuju.domain.member.entity.Member;

import com.prgrms.ijuju.global.exception.CustomException;
import com.prgrms.ijuju.domain.chat.exception.ChatException;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.Duration;

import jakarta.validation.constraints.NotNull;

@Document(collection = "chat_messages")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chat { // mongodb 엔티티

    @Id
    private Long id;
    
    @NotNull
    @Indexed
    @Field(name = "chat_room")
    private ChatRoom chatRoom;
    
    @NotNull
    @Indexed
    private Member sender;

    @Field(name = "sender_username")
    private String senderUsername;
    
    @NotNull
    @Field(name = "sender_profile_image")
    private String senderProfileImage;
    
    @NotNull
    private String content;
    
    @Field(name = "image_url")
    private String imageUrl;

    @Indexed
    @Field(name = "created_at")
    private LocalDateTime createdAt;
    
    @Field(name = "is_read")
    private boolean isRead;
    
    @Field(name = "is_deleted")
    private boolean isDeleted;

    public void markAsRead() {
        this.isRead = true;
    }

    public void delete() {
        if (!isDeletable()) {
            throw new CustomException(ChatException.MESSAGE_DELETION_TIMEOUT);
        }
        this.isDeleted = true;
        this.content = "삭제된 메시지입니다";
        this.imageUrl = null;
    }

    public boolean isDeletable() {
        return Duration.between(this.createdAt, LocalDateTime.now()).toMinutes() <= 5;
    }

    public static Chat createChatMessage(ChatRoom chatRoom, Member sender, String content, String imageUrl) {
        return Chat.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .senderUsername(sender.getUsername())
                .senderProfileImage(sender.getProfileImage())
                .content(content)
                .imageUrl(imageUrl)
                .createdAt(LocalDateTime.now())
                .isRead(false)
                .isDeleted(false)
                .build();
    }
}
