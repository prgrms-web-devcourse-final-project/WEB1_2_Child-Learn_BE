package com.prgrms.ijuju.domain.wallet.repository;

import com.prgrms.ijuju.domain.wallet.entity.ExchangeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExchangeTransactionRepository extends JpaRepository<ExchangeTransaction, Long> {
    
    List<ExchangeTransaction> findByMemberIdOrderByCreatedAtDesc(Long memberId);
} 
