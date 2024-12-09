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
public class StockInvest {

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
    private StockType stockType;
    
    @Column(nullable = false)
    private String stockName;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public StockInvest(Member member, TransactionType transactionType, Long points, PointType pointType, StockType stockType, String stockName) {
        this.member = member;
        this.transactionType = transactionType;
        this.points = points;
        this.pointType = pointType;
        this.stockType = stockType;
        this.stockName = stockName;
    }
} 
