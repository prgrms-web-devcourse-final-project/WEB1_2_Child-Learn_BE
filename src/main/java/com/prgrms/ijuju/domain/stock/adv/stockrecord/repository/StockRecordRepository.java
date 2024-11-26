package com.prgrms.ijuju.domain.stock.adv.stockrecord.repository;


import com.prgrms.ijuju.domain.stock.adv.stockrecord.entity.StockRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockRecordRepository extends JpaRepository<StockRecord, Long> {
    List<StockRecord> findByAdvId(Long advId); // 특정 AdvancedInvest ID로 거래 내역 조회

    List<StockRecord> findByAdvIdAndStockSymbol(Long advId, String stockSymbol); // 특정 주식 심볼의 거래 내역 조회

    Optional<StockRecord> findByMemberIdAndSymbol(Long memberId, String symbol);


}