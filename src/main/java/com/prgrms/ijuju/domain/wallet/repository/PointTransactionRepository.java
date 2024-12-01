package com.prgrms.ijuju.domain.wallet.repository;

import com.prgrms.ijuju.domain.wallet.entity.PointTransaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.prgrms.ijuju.domain.wallet.entity.PointType;
import com.prgrms.ijuju.domain.wallet.entity.TransactionType;
import java.time.LocalDateTime;

public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long> {
    
    List<PointTransaction> findByMemberIdOrderByCreatedAtDesc(Long memberId);
    
    List<PointTransaction> findByMemberIdAndTransactionTypeOrderByCreatedAtDesc(
        Long memberId, TransactionType transactionType);
    
    List<PointTransaction> findByMemberIdAndPointTypeOrderByCreatedAtDesc(
        Long memberId, PointType pointType);
    
    List<PointTransaction> findByMemberIdAndCreatedAtBetweenOrderByCreatedAtDesc(
        Long memberId, LocalDateTime startDate, LocalDateTime endDate);

    boolean existsByMemberIdAndPointTypeAndCreatedAtBetween(
        Long memberId, PointType pointType, LocalDateTime startDate, LocalDateTime endDate);
} 
