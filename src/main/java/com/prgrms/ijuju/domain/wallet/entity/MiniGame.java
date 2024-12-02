package com.prgrms.ijuju.domain.wallet.entity;

import com.prgrms.ijuju.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class MiniGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;

    @Column(nullable = false)
    private Long points;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PointType pointType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameType gameType;
    
    private boolean isWin;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public MiniGame(Member member, TransactionType transactionType, Long points, PointType pointType, GameType gameType, boolean isWin) {
        this.member = member;
        this.transactionType = transactionType;
        this.points = points;
        this.pointType = pointType;
        this.gameType = gameType;
        this.isWin = isWin;
    }
} 
