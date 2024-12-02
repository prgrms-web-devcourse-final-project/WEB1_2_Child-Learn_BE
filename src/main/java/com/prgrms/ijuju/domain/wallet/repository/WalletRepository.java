package com.prgrms.ijuju.domain.wallet.repository;

import com.prgrms.ijuju.domain.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.time.LocalDateTime;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    
    Optional<Wallet> findByMemberIdAndCreatedAtBetween(Long memberId, LocalDateTime startDate, LocalDateTime endDate);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Wallet> findByMemberId(Long memberId);
} 
