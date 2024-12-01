package com.prgrms.ijuju.domain.wallet.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.wallet.exception.WalletException;
import com.prgrms.ijuju.global.common.entity.BaseTimeEntity;
import com.prgrms.ijuju.global.exception.CustomException;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Wallet extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private Long currentPoints = 0L;

    @Column(nullable = false)
    private Long currentCoins = 0L;

    @Builder
    public Wallet(Member member) {
        this.member = member;
        this.currentPoints = 0L;
        this.currentCoins = 0L;
    }

    public void updatePointsAndCoins(Long points, TransactionType type) {
        validateTransaction(points, type);
        
        switch (type) {
            case EARNED -> addPoints(points);
            case USED -> subtractPoints(points);
            case EXCHANGED -> exchangePointsToCoin(points);
            case MAINTAINED -> {}
            default -> throw new CustomException(WalletException.INVALID_TRANSACTION_TYPE);
        }
    }
    
    private void validateTransaction(Long points, TransactionType type) {
        if (points < 0) {
            throw new CustomException(WalletException.INVALID_AMOUNT);
        }
        
        if (type == TransactionType.USED && this.currentPoints < points) {
            throw new CustomException(WalletException.INSUFFICIENT_POINTS);
        }
        
        if (type == TransactionType.EXCHANGED) {
            if (points < 100 || points % 100 != 0) {
                throw new CustomException(WalletException.EXCHANGE_UNIT);
            }
            if (this.currentPoints < points) {
                throw new CustomException(WalletException.INSUFFICIENT_POINTS);
            }
        }
    }

    private void addPoints(Long points) {
        this.currentPoints += points;
        if (this.currentPoints < 0) {
            throw new CustomException(WalletException.INVALID_AMOUNT);
        }
    }

    private void subtractPoints(Long points) {
        if (this.currentPoints < points) {
            throw new CustomException(WalletException.INSUFFICIENT_POINTS);
        }
        this.currentPoints -= points;
    }

    private void exchangePointsToCoin(Long points) {
        if (points < 100 || points % 100 != 0) {
            throw new CustomException(WalletException.EXCHANGE_UNIT);
        }
        subtractPoints(points);
        this.currentCoins += points / 100;
    }
}
