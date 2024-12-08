package com.prgrms.ijuju.domain.chat.entity;

import lombok.Builder;
import lombok.Getter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.chat.exception.ChatException;
import com.prgrms.ijuju.domain.chat.exception.ChatErrorCode;
import com.prgrms.ijuju.domain.chat.validation.ValidImage;

import org.springframework.web.multipart.MultipartFile;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.Duration;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat")
@CompoundIndex(def = "{'roomId': 1, 'createdAt': -1}")
public class Chat {
    
    @Id
    private String id;
    
    @NotNull
    @Indexed
    @Field(name = "room_id")
    private String roomId;
    
    @NotNull
    @Indexed
    @Field(name = "sender_id")
    private Long senderId;

    @NotNull
    @Indexed
    @Field(name = "sender_login_id")
    private String senderLoginId;

    @NotNull
    @Field(name = "sender_username")
    private String senderUsername;
    
    @NotNull
    @Field(name = "sender_profile_image")
    private String senderProfileImage;
    
    @Size(max = 1000, message = "메시지는 1000자를 초과할 수 없습니다")
    private String content;

    @ValidImage
    @Field(name = "image_url")
    private String imageUrl;

    @Field(name = "is_read")
    private boolean isRead;
    
    @Field(name = "is_deleted")
    private boolean isDeleted;

    @Indexed
    @Field(name = "created_at")
    private LocalDateTime createdAt;
    
    @Field(name = "deleted_at")
    private LocalDateTime deletedAt;

    // 메시지 생성
    public static Chat createChatMessage(ChatRoom chatRoom, Member sender, String content, String imageUrl) {
        if (content == null && imageUrl == null) {
            throw new IllegalArgumentException("메시지 내용이나 이미지 중 하나는 반드시 있어야 합니다.");
        }
        
        return Chat.builder()
                .roomId(chatRoom.getId())
                .senderLoginId(sender.getLoginId())
                .senderUsername(sender.getUsername())
                .senderProfileImage(sender.getProfileImage())
                .content(content)
                .imageUrl(imageUrl)
                .createdAt(LocalDateTime.now())
                .isRead(false)
                .isDeleted(false)
                .build();
    }

    // 메시지 삭제
    public void delete() {
        if (!canDelete(this.senderId)) {
            throw new ChatException(ChatErrorCode.MESSAGE_DELETION_TIMEOUT);
        }
        this.isDeleted = true;
        this.content = "삭제된 메시지입니다";
        this.imageUrl = null;
        this.deletedAt = LocalDateTime.now();
    }

    // 메시지 삭제 가능 여부 확인
    public boolean canDelete(Long userId) {
        return senderId.equals(userId) && 
               Duration.between(createdAt, LocalDateTime.now()).toMinutes() <= 5 &&
               !isDeleted;
    }
    
    // 메시지 읽음 처리
    public void markAsReadBy(Long userId) {
        if (!this.senderId.equals(userId)) {
            this.isRead = true;
        }
    }

    // 삭제 메시지 내용 반환
    public String getContent() {
        return isDeleted ? "삭제된 메시지입니다" : content;
    }
}
