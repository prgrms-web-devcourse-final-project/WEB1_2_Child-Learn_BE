package com.prgrms.ijuju.domain.stock.adv.stockrecord.service;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.stock.adv.stockrecord.dto.request.StockRecordRequestDto;
import com.prgrms.ijuju.domain.stock.adv.stockrecord.entity.StockRecord;

import java.util.List;
import java.util.Map;

public interface StockRecordService {

    // 거래 내역 저장
    StockRecord saveRecord(StockRecordRequestDto requestDto, Member member);

    // 특정 AdvancedInvest의 모든 거래 내역 조회
    List<StockRecord> getRecordsByAdvId(Long advId);

    // 특정 주식의 거래 내역 조회
    List<StockRecord> getRecordsByStock(Long advId, String stockSymbol);

    // 특정 주식의 보유량 계산
    double calculateOwnedStock(Long advId, String stockSymbol);

    // 모든 주식의 보유량 계산
    Map<String, Double> calculateAllOwnedStocks(Long advId);
}