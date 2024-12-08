package com.prgrms.ijuju.domain.chat.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Document(collection = "chatroom")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id
    private String id;

    @Indexed
    @Field(name = "member_id")
    private Long memberId;

    @Indexed
    @Field(name = "friend_id")
    private Long friendId;

    private boolean isDeleted = false;

    @Field(name = "deleted_at")
    private LocalDateTime deletedAt;

    private String lastMessageContent;

    private LocalDateTime lastMessageTime;

    private int unreadCount;

    @DBRef
    private List<Chat> chat = new ArrayList<>();

    // 채팅 목록 조회
    public List<Chat> getChat() {
        if (chat == null) {
            chat = new ArrayList<>();
        }
        return chat;
    }

    // 마지막 메시지 시간 조회
    public LocalDateTime getLastMessageTime() {
        return lastMessageTime != null ? lastMessageTime : LocalDateTime.now();
    }

    // 채팅방 생성
    @Builder
    public ChatRoom(Long memberId, Long friendId) {
        this.memberId = memberId;
        this.friendId = friendId;
        this.chat = new ArrayList<>();
    }

    // 채팅 메시지 추가
    public void addChat(Chat newChat) {
        this.chat.add(newChat);
        updateLastMessage(newChat);
    }

    // 채팅방 마지막 메시지 조회
    public Chat getLastMessage() {
        if (chat == null || chat.isEmpty()) {
            return null;
        }
        
        return chat.stream()
                .max(Comparator.comparing(Chat::getCreatedAt))
                .orElse(null);
    }

    // 읽지 않은 메시지 수 계산
    public int getUnreadCount(Long userId) {
        return (int) chat.stream()
            .filter(chat -> !chat.isDeleted())
            .filter(chat -> !chat.getSenderId().equals(userId))
            .filter(chat -> !chat.isRead())
            .count();
    }

    // 마지막 메시지 정보 업데이트
    private void updateLastMessage(Chat chat) {
        if (!chat.isDeleted()) {
            this.lastMessageContent = chat.getContent();
            this.lastMessageTime = chat.getCreatedAt();
        }
    }

    // 메시지 읽음 처리
    public void markMessagesAsRead(Long userId) {
        chat.stream()
            .filter(message -> !message.isDeleted())
            .filter(message -> !message.getSenderId().equals(userId))
            .filter(message -> !message.isRead())
            .forEach(message -> message.markAsReadBy(userId));
        
        this.unreadCount = getUnreadCount(userId);
    }

    // 채팅방 삭제
    public void markAsDeleted() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    // 채팅방 삭제 복구
    public void restore() {
        this.deletedAt = null;
        this.isDeleted = false;
    }

    // 채팅방 삭제 여부 확인
    public boolean isDeleted() {
        return isDeleted;
    }
}
