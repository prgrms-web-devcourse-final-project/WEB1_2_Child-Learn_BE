package com.prgrms.ijuju.domain.stock.begin.repository;

import com.prgrms.ijuju.domain.stock.begin.entity.BeginStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BeginStockGraphRepository extends JpaRepository<BeginStock, Long> {
    @Query("SELECT b.price FROM BeginStock b ORDER BY b.beginId DESC")
    Optional<Integer> findPriceByOrderByBeginIdDesc();
}
