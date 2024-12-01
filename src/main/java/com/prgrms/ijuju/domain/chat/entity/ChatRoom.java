package com.prgrms.ijuju.domain.chat.entity;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.global.common.BaseTimeEntity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id")
    private Member friend;

    private boolean isDeleted;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<Chat> chats = new ArrayList<>();

    @Column(name = "last_message")
    private String lastMessage;

    @Column(name = "last_message_time")
    private LocalDateTime lastMessageTime;

    @Column(name = "unread_count")
    private int unreadCount;

    @Builder
    public ChatRoom(Member member, Member friend) {
        this.member = member;
        this.friend = friend;
        this.isDeleted = false;
        this.unreadCount = 0;
    }

    public void updateLastMessage(Chat chat) {
        this.lastMessage = chat.getContent();
        this.lastMessageTime = chat.getCreatedAt();
    }

    public void incrementUnreadCount() {
        this.unreadCount++;
    }

    public void resetUnreadCount() {
        this.unreadCount = 0;
    }

    public void delete() {
        this.isDeleted = true;
    }

    public void restore() {
        this.isDeleted = false;
    }

    public Chat getLastMessage() {
        return chats.stream()
            .filter(chat -> !chat.isDeleted())
            .max(Comparator.comparing(Chat::getCreatedAt))
            .orElse(null);
    }

    public int getUnreadCount(Long userId) {
        return (int) chats.stream()
            .filter(chat -> !chat.isDeleted())
            .filter(chat -> !chat.getSender().getId().equals(userId))
            .filter(chat -> !chat.isRead())
            .count();
    }
}
