package com.prgrms.ijuju.domain.stock.adv.advstock.controller;

import com.prgrms.ijuju.domain.stock.adv.advstock.entity.AdvStock;
import com.prgrms.ijuju.domain.stock.adv.advstock.scheduler.AdvStockScheduler;
import com.prgrms.ijuju.domain.stock.adv.advstock.service.AdvStockDataFetcher;
import com.prgrms.ijuju.domain.stock.adv.advstock.service.AdvStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 실제 서비스 중 StockController 가 사용 될 일은 없습니다. 이는 순수하게 테스트 용도라고 생각해 주시면 될 것 같습니다
 */

@RestController
@RequestMapping("/api/v1/test/stocks")
@RequiredArgsConstructor
public class AdvStockController {

    private final AdvStockService advStockService;
    private final AdvStockDataFetcher advStockDataFetcher;
    private final AdvStockScheduler advStockScheduler;


    @GetMapping("/reference")
    public ResponseEntity<List<AdvStock>> getReferenceData() {
        List<AdvStock> referenceData = advStockService.getReferenceData();
        return ResponseEntity.ok(referenceData);
    }


    @GetMapping("/live/{hour}")
    public ResponseEntity<AdvStock> getLiveData(
            @RequestParam String symbol,
            @PathVariable int hour) {
        AdvStock liveData = advStockService.getLiveData(symbol, hour);
        return ResponseEntity.ok(liveData);
    }

    @PostMapping("/trigger-scheduler")
    public ResponseEntity<String> triggerSchedulerManually() {
        advStockScheduler.fetchAndUpdateStockDataDaily(); // 기존 Scheduler 메서드 호출
        return ResponseEntity.ok("스케줄러 로직이 수동으로 실행되었습니다.");
    }


}