package com.prgrms.ijuju.domain.wallet.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.wallet.exception.WalletException;
import com.prgrms.ijuju.domain.wallet.exception.WalletErrorCode;
import com.prgrms.ijuju.global.common.entity.BaseTimeEntity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Wallet extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private Long currentPoints;

    @Column(nullable = false)
    private Long currentCoins;

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
            default -> throw new WalletException(WalletErrorCode.TRANSACTION_INVALID_TYPE);
        }
    }
    
    private void validateTransaction(Long points, TransactionType type) {
        if (points < 0) {
            throw new WalletException(WalletErrorCode.TRANSACTION_AMOUNT_INVALID);
        }
        
        if (type == TransactionType.USED && this.currentPoints < points) {
            throw new WalletException(WalletErrorCode.POINT_INSUFFICIENT);
        }
        
        if (type == TransactionType.EXCHANGED) {
            if (points < 100 || points % 100 != 0) {
                throw new WalletException(WalletErrorCode.EXCHANGE_MINIMUM_AMOUNT);
            }
            if (this.currentPoints < points) {
                throw new WalletException(WalletErrorCode.POINT_INSUFFICIENT);
            }
        }
    }

    private void addPoints(Long points) {
        this.currentPoints += points;
        if (this.currentPoints < 0) {
            throw new WalletException(WalletErrorCode.POINT_NEGATIVE_NOT_ALLOWED);
        }
    }

    private void subtractPoints(Long points) {
        if (this.currentPoints < points) {
            throw new WalletException(WalletErrorCode.POINT_INSUFFICIENT);
        }
        this.currentPoints -= points;
    }

    private void exchangePointsToCoin(Long points) {
        if (points < 100 || points % 100 != 0) {
            throw new WalletException(WalletErrorCode.EXCHANGE_MINIMUM_AMOUNT);
        }
        subtractPoints(points);
        this.currentCoins += points / 100;
    }

    public void subtractCoins(Long coins) {
        if (this.currentCoins < coins) {
            throw new WalletException(WalletErrorCode.COIN_INSUFFICIENT);
        }
        this.currentCoins -= coins;
    }
}
