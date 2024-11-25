package com.prgrms.ijuju.domain.stock.mid.repository;

import com.prgrms.ijuju.domain.stock.mid.entity.MidStock;
import com.prgrms.ijuju.domain.stock.mid.entity.MidStockPrice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;


public interface MidStockPriceRepository extends JpaRepository<MidStockPrice, Long>, MidStockPriceRepositoryCustom {

//    @Query("SELECT p FROM MidStockPrice p WHERE p.midStock.id = :stockId")
//    List<MidStockPrice> findByMidStockId(@Param("stockId") Long stockId);

    @Modifying
    @Query("DELETE FROM MidStockPrice  p WHERE p.priceDate < :date")
    void deleteOldData(@Param("date")LocalDateTime date);

//    @Query("SELECT p FROM MidStockPrice p WHERE p.midStock = :stock ORDER BY p.priceDate DESC LIMIT 1")
//    Optional<MidStockPrice> findLatestPrice(@Param("stock") MidStock stock);

//    @Query("SELECT p FROM MidStockPrice p WHERE p.midStock = :stock ORDER BY p.priceDate DESC")
//    List<MidStockPrice> findPriceHistory(@Param("stock") MidStock stock, Pageable pageable);

//    @Query("SELECT p FROM MidStockPrice p WHERE p.midStock.id = :midStockId ORDER BY p.priceDate LIMIT 14")
//    List<MidStockPrice> find2WeeksPriceInfo(@Param("midStockId") Long midStockId);

//    @Query("SELECT p FROM MidStockPrice p " +
//            "WHERE p.midStock.id = :stockId " +
//            "AND DATE(p.priceDate) = CURRENT_DATE " +
//            "ORDER BY p.priceDate DESC LIMIT 1")
//    Optional<MidStockPrice> findTodayPrice(@Param("stockId") Long stockId);

    // 오늘 날짜의 평균 가격 가져오기
    @Query("SELECT p.avgPrice FROM MidStockPrice p " +
            "WHERE p.midStock.id = :stockId " +
            "AND FUNCTION('DATE', p.priceDate) = CURRENT_DATE")
    long findTodayAvgPrice(@Param("stockId") Long stockId);

}
