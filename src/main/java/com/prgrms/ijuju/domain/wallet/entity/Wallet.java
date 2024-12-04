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
            default -> throw WalletException.INVALID_TRANSACTION_TYPE.toException();
        }
    }
    
    private void validateTransaction(Long points, TransactionType type) {
        if (points < 0) {
            throw WalletException.INVALID_AMOUNT.toException();
        }
        
        if (type == TransactionType.USED && this.currentPoints < points) {
            throw WalletException.INSUFFICIENT_POINTS.toException();
        }
        
        if (type == TransactionType.EXCHANGED) {
            if (points < 100 || points % 100 != 0) {
                throw WalletException.EXCHANGE_UNIT.toException();
            }
            if (this.currentPoints < points) {
                throw WalletException.INSUFFICIENT_POINTS.toException();
            }
        }
    }

    private void addPoints(Long points) {
        this.currentPoints += points;
        if (this.currentPoints < 0) {
            throw WalletException.INVALID_AMOUNT.toException();
        }
    }

    private void subtractPoints(Long points) {
        if (this.currentPoints < points) {
            throw WalletException.INSUFFICIENT_POINTS.toException();
        }
        this.currentPoints -= points;
    }

    private void exchangePointsToCoin(Long points) {
        if (points < 100 || points % 100 != 0) {
            throw WalletException.EXCHANGE_UNIT.toException();
        }
        subtractPoints(points);
        this.currentCoins += points / 100;
    }

    public void subtractCoins(Long coins) {
        if (this.currentCoins < coins) {
            throw WalletException.INSUFFICIENT_POINTS.toException();
        }
        this.currentCoins -= coins;
    }
}
