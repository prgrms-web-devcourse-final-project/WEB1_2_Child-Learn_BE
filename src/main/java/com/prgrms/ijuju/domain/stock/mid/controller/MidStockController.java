package com.prgrms.ijuju.domain.stock.mid.controller;

import com.prgrms.ijuju.domain.stock.mid.dto.response.MidStockResponse;
import com.prgrms.ijuju.domain.stock.mid.dto.response.MidStockTradeInfo;
import com.prgrms.ijuju.domain.stock.mid.dto.response.MidStockWithTradesResponse;
import com.prgrms.ijuju.domain.stock.mid.service.MidStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/v1/mid-stocks")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MidStockController {
    private final MidStockService midStockService;

    // 중급거래목록 3가지 가져오기
    @GetMapping("/list")
    public ResponseEntity<List<MidStockResponse>> findAllStocks() {
        List<MidStockResponse> stocks = midStockService.findAllStocks();
        return ResponseEntity.ok(stocks);
    }

    // 해당하는 멤버의 특정 주식 내역 가져오기
    // 나중에 jwt로 인증 + memberId 뽑기
    @GetMapping("/{midStockId}")
    public ResponseEntity<List<MidStockTradeInfo>> findStockTrades(@PathVariable Long midStockId) {
        Long memberId = 1L; // 임시로 memberId 1로 설정
        List<MidStockTradeInfo> trades = midStockService.findStockTrades(memberId, midStockId);
        return ResponseEntity.ok(trades);
    }

    // 모든 보유 주식 조회
    // 나중에 jwt로 인증 + memberId 뽑기
    @GetMapping
    public ResponseEntity<List<MidStockWithTradesResponse>> getMemberStocksAndTrades() {
        Long memberId = 1L; // 임시로 memberId 1로 설정
        List<MidStockWithTradesResponse> stocksAndTrades = midStockService.getMemberStocksAndTrades(memberId);
        return ResponseEntity.ok(stocksAndTrades);
    }








}
