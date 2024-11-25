package com.prgrms.ijuju.domain.point.controller;

import com.prgrms.ijuju.domain.point.dto.request.PointRequestDTO;
import com.prgrms.ijuju.domain.point.dto.response.PointResponseDTO;
import com.prgrms.ijuju.domain.point.entity.ExchangeDetails;
import com.prgrms.ijuju.domain.point.entity.GameType;
import com.prgrms.ijuju.domain.point.entity.PointDetails;
import com.prgrms.ijuju.domain.point.entity.PointStatus;
import com.prgrms.ijuju.domain.point.entity.PointType;
import com.prgrms.ijuju.domain.point.entity.StockStatus;
import com.prgrms.ijuju.domain.point.entity.StockType;
import com.prgrms.ijuju.domain.point.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/point")
public class PointController {

    @Autowired
    private PointService pointService;

    // 현재 포인트 및 코인 조회
    @GetMapping("/current/{memberId}")
    public ResponseEntity<PointResponseDTO> getCurrentPointsAndCoins(@PathVariable Long memberId) {
        PointResponseDTO response = pointService.CurrentPointsAndCoins(memberId);
        return ResponseEntity.ok(response);
    }
    
    // 미니게임 결과 처리
    @PostMapping("/game")
    public ResponseEntity<PointResponseDTO> playMiniGame(@RequestBody PointRequestDTO request, GameType gameType) {
        PointResponseDTO response = pointService.playMiniGame(request.getMemberId(), gameType, request.getPointAmount(), true);
        return ResponseEntity.ok(response);
    }

    // 주식 투자 결과 처리
    @PostMapping("/stock")
    public ResponseEntity<PointResponseDTO> simulateStockInvestment(@RequestBody PointRequestDTO request, StockType stockType, StockStatus stockStatus) {
        PointResponseDTO response = pointService.simulateStockInvestment(request, stockType, stockStatus);
        return ResponseEntity.ok(response);
    }

    // 포인트를 코인으로 환전
    @PostMapping("/exchange")
    public ResponseEntity<PointResponseDTO> exchangePointsToCoins(@RequestBody PointRequestDTO request) {
        PointResponseDTO response = pointService.exchangePointsToCoins(request.getMemberId(), request.getPointAmount());
        return ResponseEntity.ok(response);
    }

    // 포인트 거래 내역 조회 (포인트타입/포인트상태/주간별 필터링)
    @GetMapping("/history/{memberId}")
    public ResponseEntity<List<PointDetails>> getFilteredPointHistory(@PathVariable Long memberId,
                                                                      @RequestParam(required = false) PointType type,
                                                                      @RequestParam(required = false) PointStatus status,
                                                                      @RequestParam(required = false) Integer weekOfYear) {
        List<PointDetails> history = pointService.getFilteredPointHistory(memberId, type, status, weekOfYear);
        return ResponseEntity.ok(history);
    }

    // 포인트에서 코인으로 환전된 거래내역 조회
    @GetMapping("/exchange-history/{memberId}")
    public ResponseEntity<List<ExchangeDetails>> getExchangeHistory(@PathVariable Long memberId) {
        List<ExchangeDetails> exchangeHistory = pointService.getExchangeHistory(memberId);
        return ResponseEntity.ok(exchangeHistory);
    }
} 