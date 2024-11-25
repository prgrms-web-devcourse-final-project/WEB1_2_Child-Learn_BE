package com.prgrms.ijuju.domain.friend.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.prgrms.ijuju.domain.member.entity.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "send_id", nullable = false)
    @NotNull
    private Member sender; // 친구 요청한 사람

    @ManyToOne
    @JoinColumn(name = "receive_id", nullable = false)
    @NotNull
    private Member receiver; // 친구 요청 받은 사람

    @Enumerated(EnumType.STRING)
    private RequestStatus status; // 친구 요청 상태

    @CreatedDate
    private LocalDateTime createdAt; // 친구 요청 시간

    private LocalDateTime updatedAt;
}
