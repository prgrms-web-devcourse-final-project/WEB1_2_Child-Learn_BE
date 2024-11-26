package com.prgrms.ijuju.domain.stock.begin.repository;

import com.prgrms.ijuju.domain.stock.begin.entity.LimitBeginStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LimitBeginStockRepository extends JpaRepository<LimitBeginStock, Long> {
    Optional<LimitBeginStock> findByMemberId(Long memberId);
}
