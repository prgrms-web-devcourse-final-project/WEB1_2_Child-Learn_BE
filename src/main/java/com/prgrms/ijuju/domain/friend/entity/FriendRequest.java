package com.prgrms.ijuju.domain.friend.entity;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.friend.exception.FriendException;
import com.prgrms.ijuju.global.common.entity.BaseTimeEntity;
import com.prgrms.ijuju.global.exception.CustomException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.FetchType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "friend_request")
public class FriendRequest extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private Member receiver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus requestStatus;

    @Column(nullable = false)
    private boolean isRead;

    @Builder
    public FriendRequest(Member sender, Member receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.requestStatus = RequestStatus.PENDING;
        this.isRead = false;
    }

    public boolean isPending() {
        return this.requestStatus == RequestStatus.PENDING;
    }

    public void accept() {
        validatePendingStatus();
        this.requestStatus = RequestStatus.ACCEPTED;
    }

    public void reject() {
        validatePendingStatus();
        this.requestStatus = RequestStatus.REJECTED;
    }

    public void markAsRead() {
        this.isRead = true;
    }

    private void validatePendingStatus() {
        if (this.requestStatus != RequestStatus.PENDING) {
            throw new CustomException(FriendException.REQUEST_ALREADY_PROCESSED);
        }
    }
}
