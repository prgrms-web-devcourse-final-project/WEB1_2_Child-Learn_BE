package com.prgrms.ijuju.domain.stock.begin.repository;

import com.prgrms.ijuju.domain.stock.begin.entity.BeginStockPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BeginStockPriceRepository extends JpaRepository<BeginStockPrice, Long> {
    Optional<BeginStockPrice> findTopByOrderByBeginIdDesc();

    @Query("SELECT b FROM BeginStockPrice b WHERE b.stockDate BETWEEN :startDate AND :endDate")
    List<BeginStockPrice> find7BeginStockData(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
