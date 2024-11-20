package com.prgrms.ijuju.stock.adv.stock.controller;

import com.prgrms.ijuju.stock.adv.stock.constant.DataType;
import com.prgrms.ijuju.stock.adv.stock.dto.PolygonCandleResponse;
import com.prgrms.ijuju.stock.adv.stock.entity.Stock;
import com.prgrms.ijuju.stock.adv.stock.scheduler.StockScheduler;
import com.prgrms.ijuju.stock.adv.stock.service.StockDataFetcher;
import com.prgrms.ijuju.stock.adv.stock.service.StockService;
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
public class StockController {

    private final StockService stockService;
    private final StockDataFetcher stockDataFetcher;
    private final StockScheduler stockScheduler;


    @GetMapping("/reference")
    public ResponseEntity<List<Stock>> getReferenceData() {
        List<Stock> referenceData = stockService.getReferenceData();
        return ResponseEntity.ok(referenceData);
    }


    @GetMapping("/live/{hour}")
    public ResponseEntity<Stock> getLiveData(
            @RequestParam String symbol,
            @PathVariable int hour) {
        Stock liveData = stockService.getLiveData(symbol, hour);
        return ResponseEntity.ok(liveData);
    }

    @PostMapping("/trigger-scheduler")
    public ResponseEntity<String> triggerSchedulerManually() {
        stockScheduler.fetchAndUpdateStockDataDaily(); // 기존 Scheduler 메서드 호출
        return ResponseEntity.ok("스케줄러 로직이 수동으로 실행되었습니다.");
    }


}