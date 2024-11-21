package com.prgrms.ijuju.domain.stock.mid.repository;

import com.prgrms.ijuju.domain.stock.mid.entity.MidStockTrade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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

}
