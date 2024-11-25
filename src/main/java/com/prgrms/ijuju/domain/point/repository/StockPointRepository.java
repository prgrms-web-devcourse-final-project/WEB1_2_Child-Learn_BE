package com.prgrms.ijuju.domain.point.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.prgrms.ijuju.domain.point.entity.StockPoint;

@Repository
public interface StockPointRepository extends JpaRepository<StockPoint, Long> {
} 