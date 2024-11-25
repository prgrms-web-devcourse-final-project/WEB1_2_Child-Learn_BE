package com.prgrms.ijuju.domain.stock.adv.advancedinvest.controller;

import com.prgrms.ijuju.domain.stock.adv.advancedinvest.dto.request.AdvancedInvestRequestDto;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.dto.response.AdvancedInvestResponseDto;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.service.AdvancedInvestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/advanced-invest")
@RequiredArgsConstructor
public class AdvancedInvestController {

    private final AdvancedInvestService advancedInvestService;

    // 게임 시작
    @PostMapping("/start")
    public ResponseEntity<AdvancedInvestResponseDto> startGame(@RequestBody AdvancedInvestRequestDto request) {
        AdvancedInvestResponseDto response = advancedInvestService.startGame(request);
        return ResponseEntity.ok(response);
    }

    // 게임 일시정지
    @PostMapping("/{advId}/pause")
    public ResponseEntity<Void> pauseGame(@PathVariable Long advId) {
        advancedInvestService.pauseGame(advId);
        return ResponseEntity.ok().build();
    }

    // 게임 재개
    @PostMapping("/{advId}/resume")
    public ResponseEntity<Void> resumeGame(@PathVariable Long advId) {
        advancedInvestService.resumeGame(advId);
        return ResponseEntity.ok().build();
    }

    // Reference 데이터 가져오기
    @GetMapping("/{advId}/reference")
    public ResponseEntity<List<StockResponseDto>> getReferenceData(@PathVariable Long advId) {
        List<StockResponseDto> referenceData = advancedInvestService.getReferenceData(advId);
        return ResponseEntity.ok(referenceData);
    }

    // Live 데이터 가져오기
    @GetMapping("/{advId}/live")
    public ResponseEntity<StockResponseDto> getLiveData(
            @PathVariable Long advId,
            @RequestParam String symbol,
            @RequestParam int hour) {
        StockResponseDto liveData = advancedInvestService.getLiveData(advId, symbol, hour);
        return ResponseEntity.ok(liveData);
    }

    // 주식 구매
    @PostMapping("/{advId}/buy")
    public ResponseEntity<Void> buyStock(@PathVariable Long advId, @RequestBody StockTransactionRequestDto request) {
        advancedInvestService.buyStock(advId, request);
        return ResponseEntity.ok().build();
    }

    // 주식 판매
    @PostMapping("/{advId}/sell")
    public ResponseEntity<Void> sellStock(@PathVariable Long advId, @RequestBody StockTransactionRequestDto request) {
        advancedInvestService.sellStock(advId, request);
        return ResponseEntity.ok().build();
    }
}

