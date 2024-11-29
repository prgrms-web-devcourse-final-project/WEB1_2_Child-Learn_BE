package com.prgrms.ijuju.domain.stock.mid.controller;

import com.prgrms.ijuju.domain.stock.mid.dto.request.MidStockTradePointRequest;
import com.prgrms.ijuju.domain.stock.mid.dto.response.*;
import com.prgrms.ijuju.domain.stock.mid.service.MidStockService;
import com.prgrms.ijuju.domain.stock.mid.service.MidStockTradeService;
import com.prgrms.ijuju.global.auth.SecurityUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/mid-stocks")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MidStockController {
    private final MidStockService midStockService;
    private final MidStockTradeService midStockTradeService;

    // 중급거래목록 3가지 가져오기
    @GetMapping("/list")
    public ResponseEntity<List<MidStockResponse>> findAllStocks() {
        log.info("중급 주식 목록 조회");
        List<MidStockResponse> stocks = midStockService.findAllStocks();

        return ResponseEntity.ok(stocks);
    }

    // 해당하는 멤버의 특정 주식 내역 가져오기
    @GetMapping("/{midStockId}")
    public ResponseEntity<List<MidStockTradeInfo>> findStockTrades(@PathVariable Long midStockId, @AuthenticationPrincipal SecurityUser securityUser) {
        log.info("중급 특정 주식 내역 조회");
        Long memberId = securityUser.getId();
        List<MidStockTradeInfo> trades = midStockService.findStockTrades(memberId, midStockId);

        return ResponseEntity.ok(trades);
    }

    // 모든 보유 주식 조회
    @GetMapping
    public ResponseEntity<List<MidStockWithTradesResponse>> findAllStockTrades(@AuthenticationPrincipal SecurityUser securityUser) {
        log.info("중급 모든 보유 주식 조회");
        Long memberId = securityUser.getId();
        List<MidStockWithTradesResponse> stocksAndTrades = midStockService.getMemberStocksAndTrades(memberId);

        return ResponseEntity.ok(stocksAndTrades);
    }

    // 2주치 가격 데이터 제공
    @GetMapping("/{midStockId}/price")
    public ResponseEntity<List<MidStockPriceResponse>> findStockPrices(@PathVariable Long midStockId) {
        List<MidStockPriceResponse> prices = midStockService.findStockChartInfo(midStockId);

        return ResponseEntity.ok(prices);
    }

    // 오늘 거래가능한지 확인
    @GetMapping("/{midStockId}/available")
    public ResponseEntity<TradeAvailableResponse> isTradeAvailable(@PathVariable Long midStockId, @AuthenticationPrincipal SecurityUser securityUser) {
        log.info("중급 주식 거래 가능 여부 확인");
        Long memberId = securityUser.getId();
        TradeAvailableResponse response = midStockTradeService.isTradeAvailable(memberId, midStockId);
        return ResponseEntity.ok(response);
    }

    //매수
    @PostMapping("/{midStockId}/buy")
    public ResponseEntity<Map<String, Boolean>> buyMidStock(@PathVariable @Positive Long midStockId, @RequestBody @Valid MidStockTradePointRequest tradePointRequest, @AuthenticationPrincipal SecurityUser securityUser) {
        log.info("중급 주식 매수 요청");
        Long memberId = securityUser.getId();
        long tradePoint = tradePointRequest.getTradePoint();
        boolean warning = midStockTradeService.buyStock(memberId, midStockId, tradePoint);
        Map<String, Boolean> response = Map.of("warning", warning);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 매도
    @PostMapping("/{midStockId}/sell")
    public ResponseEntity<Map<String, Long>> sellMidStock(@PathVariable @Positive Long midStockId, @AuthenticationPrincipal SecurityUser securityUser) {
        log.info("중급 주식 매도 요청");
        Long memberId = securityUser.getId();
        long profit = midStockTradeService.sellStock(memberId, midStockId);
        Map<String, Long> response = Map.of("earnedPoints", profit);

        return ResponseEntity.ok(response);
    }
}
