package com.prgrms.ijuju.domain.wallet.entity;

import com.prgrms.ijuju.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class ExchangeTransaction {
 
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
    private Long pointsExchanged;

    @Column(nullable = false)
    private Long coinsReceived;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PointType pointType;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public ExchangeTransaction(Member member, TransactionType transactionType, Long pointsExchanged, Long coinsReceived, PointType pointType) {
        this.member = member;
        this.transactionType = transactionType;
        this.pointsExchanged = pointsExchanged;
        this.coinsReceived = coinsReceived;
        this.pointType = pointType;
    }
}
