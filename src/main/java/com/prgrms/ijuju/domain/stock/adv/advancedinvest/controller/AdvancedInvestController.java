package com.prgrms.ijuju.domain.stock.adv.advancedinvest.controller;

import com.prgrms.ijuju.domain.stock.adv.advancedinvest.dto.request.AdvancedInvestRequestDto;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.dto.request.StockTransactionRequestDto;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.dto.response.AdvancedInvestResponseDto;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.dto.response.StockResponseDto;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.service.AdvancedInvestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * WebSocket 사용을 하기 때문에 해당 컨트롤러는 더이상 사용되지 않습니다.
 */

/*
@RestController
@RequestMapping("/api/v1/advanced-invest")
@RequiredArgsConstructor
public class AdvancedInvestController {

    private final AdvancedInvestService advancedInvestService;

    @PostMapping("/start")
    public ResponseEntity<AdvancedInvestResponseDto> startGame(@RequestBody AdvancedInvestRequestDto request) {
        AdvancedInvestResponseDto response = advancedInvestService.startGame(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{advId}/pause")
    public ResponseEntity<Void> pauseGame(@PathVariable Long advId) {
        advancedInvestService.pauseGame(advId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{advId}/resume")
    public ResponseEntity<Void> resumeGame(@PathVariable Long advId) {
        advancedInvestService.resumeGame(advId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{advId}/end")
    public ResponseEntity<Void> endGame(@PathVariable Long advId) {
        advancedInvestService.endGame(advId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{advId}/reference")
    public ResponseEntity<List<StockResponseDto>> getReferenceData(@PathVariable Long advId) {
        List<StockResponseDto> referenceData = advancedInvestService.getReferenceData(advId);
        return ResponseEntity.ok(referenceData);
    }

    @GetMapping("/{advId}/live")
    public ResponseEntity<StockResponseDto> getLiveData(
            @PathVariable Long advId,
            @RequestParam String symbol,
            @RequestParam int hour) {
        StockResponseDto liveData = advancedInvestService.getLiveData(advId, symbol, hour);
        return ResponseEntity.ok(liveData);
    }

    @PostMapping("/{advId}/buy")
    public ResponseEntity<Void> buyStock(@PathVariable Long advId, @RequestBody StockTransactionRequestDto request) {
        advancedInvestService.buyStock(advId, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{advId}/sell")
    public ResponseEntity<Void> sellStock(@PathVariable Long advId, @RequestBody StockTransactionRequestDto request) {
        advancedInvestService.sellStock(advId, request);
        return ResponseEntity.ok().build();
    }
}

 */