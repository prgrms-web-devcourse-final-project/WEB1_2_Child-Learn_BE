package com.prgrms.ijuju.domain.stock.begin.repository;

import com.prgrms.ijuju.domain.stock.begin.entity.BeginStockGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BeginStockGraphRepository extends JpaRepository<BeginStockGraph, Long> {
    Optional<BeginStockGraph> findTopByOrderByBeginIdDesc();

    @Query("SELECT b FROM BeginStockGraph b WHERE b.stockDate BETWEEN :startDate AND :endDate")
    List<BeginStockGraph> find7BeginStockData(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
