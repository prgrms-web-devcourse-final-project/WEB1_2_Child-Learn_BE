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

    // 보유중인 종목의 거래내역 조회
//    @Query("SELECT t FROM MidStockTrade t " +
//            "WHERE t.member.id = :memberId " +
//            "AND t.tradeType = 'BUY'")
//    List<MidStockTrade> findAllBuyMidStock(@Param("memberId") Long memberId);
}
