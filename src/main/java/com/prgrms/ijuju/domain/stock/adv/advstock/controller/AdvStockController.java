package com.prgrms.ijuju.domain.stock.adv.advstock.controller;

import com.prgrms.ijuju.domain.stock.adv.advstock.constant.DataType;
import com.prgrms.ijuju.domain.stock.adv.advstock.entity.AdvStock;
import com.prgrms.ijuju.domain.stock.adv.advstock.repository.AdvStockRepository;
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
@RequestMapping("/api/v1/adv-stocks")
@RequiredArgsConstructor
public class AdvStockController {

    private final AdvStockScheduler advStockScheduler;
    private final AdvStockRepository advStockRepository;


    @DeleteMapping("/delete-all")
    public ResponseEntity<String> deleteAllStockData() {
        // Delete REFERENCE data
        advStockRepository.deleteByDataType(DataType.REFERENCE);
        // Delete LIVE data
        advStockRepository.deleteByDataType(DataType.LIVE);

        return ResponseEntity.ok("All stock data has been successfully deleted.");
    }

    @PostMapping("/trigger-scheduler")
    public ResponseEntity<String> triggerSchedulerManually() {
        advStockScheduler.fetchAndUpdateStockDataDaily(); // 기존 Scheduler 메서드 호출
        return ResponseEntity.ok("스케줄러 로직이 수동으로 실행되었습니다.");
    }


}