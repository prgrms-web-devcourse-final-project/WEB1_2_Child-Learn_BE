package com.prgrms.ijuju.domain.chat.entity;

import com.prgrms.ijuju.domain.chat.exception.ChatException;
import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.global.common.entity.BaseTimeEntity;
import com.prgrms.ijuju.global.exception.CustomException;
import java.time.Duration;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Member sender;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "image_url")
    private String imageUrl;

    private boolean isRead;

    private boolean isDeleted;

    @Column(name = "elapsed_time")
    private Long elapsedTime;

    @Builder
    public Chat(ChatRoom chatRoom, Member sender, String content, String imageUrl) {
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.content = content;
        this.imageUrl = imageUrl;
        this.isRead = false;
        this.isDeleted = false;
        this.elapsedTime = 0L;
    }

    public void markAsRead() {
        this.isRead = true;
    }

    @PrePersist
    @PreUpdate
    public void updateElapsedTime() {
        if (this.getCreatedAt() != null) {
            this.elapsedTime = Duration.between(this.getCreatedAt(), LocalDateTime.now()).toMinutes();
        }
    }

    public boolean isDeletable() {
        return Duration.between(this.getCreatedAt(), LocalDateTime.now()).toMinutes() <= 5;
    }

    public void delete() {
        if (!isDeletable()) {
            throw new CustomException(ChatException.MESSAGE_DELETION_TIMEOUT.getMessage());
        }
        this.isDeleted = true;
        this.content = "삭제된 메시지입니다";
        this.imageUrl = null;
    }
}
