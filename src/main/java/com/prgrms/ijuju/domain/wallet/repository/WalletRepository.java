package com.prgrms.ijuju.domain.wallet.repository;

import com.prgrms.ijuju.domain.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    
    Optional<Wallet> findByMemberId(Long memberId);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Wallet w WHERE w.member.id = :memberId")
    Optional<Wallet> findByMemberIdWithLock(@Param("memberId") Long memberId);
} 
