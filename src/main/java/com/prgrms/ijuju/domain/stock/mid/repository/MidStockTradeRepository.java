package com.prgrms.ijuju.domain.stock.mid.repository;

import com.prgrms.ijuju.domain.stock.mid.entity.MidStockTrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MidStockTradeRepository extends JpaRepository<MidStockTrade, Long> {

    // 보유중인 모든 종목 조회
    @Query("SELECT t FROM MidStockTrade t " +
            "WHERE t.member.id = :memberId " +
            "AND t.tradeType = 'BUY'")
    List<MidStockTrade> findAllBuyMidStock(@Param("memberId") Long memberId);

    // 보유중인 특정 종목 조회
    @Query("SELECT t FROM MidStockTrade t " +
            "WHERE t.member.id = :memberId " +
            "AND t.tradeType = 'BUY' " +
            "AND t.midStock.id = :midStockId")
    List<MidStockTrade> findBuyMidStock(@Param("memberId") Long memberId, @Param("midStockId") Long midStockId);

    //오늘 매수했는지 확인
    @Query("SELECT t FROM MidStockTrade t " +
            "WHERE t.member.id = :memberId " +
            "AND t.midStock.id = :midStockId " +
            "AND FUNCTION('DATE', t.createDate) = CURRENT_DATE")
    Optional<MidStockTrade> findTodayBuyMidStock(@Param("memberId") Long memberId, @Param("midStockId") Long midStockId);

    // 오늘 매도했는지 확인
    @Query("SELECT t FROM MidStockTrade t " +
            "WHERE t.member.id = :memberId " +
            "AND t.midStock.id = :midStockId " +
            "AND t.tradeType = 'SELL' " +
            "AND FUNCTION('DATE', t.modifiedDate) = CURRENT_DATE")
    Optional<MidStockTrade> findTodaySellMidStock(@Param("memberId") Long memberId, @Param("midStockId") Long midStockId);
}
