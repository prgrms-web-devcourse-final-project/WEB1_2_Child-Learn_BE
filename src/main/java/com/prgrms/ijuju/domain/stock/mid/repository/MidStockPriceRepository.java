package com.prgrms.ijuju.domain.stock.mid.repository;

import com.prgrms.ijuju.domain.stock.mid.entity.MidStockPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;


public interface MidStockPriceRepository extends JpaRepository<MidStockPrice, Long>, MidStockPriceRepositoryCustom {

    @Modifying
    @Query("DELETE FROM MidStockPrice  p WHERE p.priceDate < :date")
    void deleteOldData(@Param("date")LocalDateTime date);

    // 오늘 날짜의 평균 가격 가져오기
    @Query("SELECT p.avgPrice FROM MidStockPrice p " +
            "WHERE p.midStock.id = :stockId " +
            "AND FUNCTION('DATE', p.priceDate) = CURRENT_DATE")
    long findTodayAvgPrice(@Param("stockId") Long stockId);

}
