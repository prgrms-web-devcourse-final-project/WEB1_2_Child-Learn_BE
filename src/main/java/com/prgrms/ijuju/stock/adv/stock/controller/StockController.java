package com.prgrms.ijuju.stock.adv.stock.controller;

import com.prgrms.ijuju.stock.adv.stock.constant.DataType;
import com.prgrms.ijuju.stock.adv.stock.dto.FinnhubCandleResponse;
import com.prgrms.ijuju.stock.adv.stock.entity.Stock;
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


    @PostMapping("/fetch")
    public ResponseEntity<Stock> fetchAndSaveStockData(
            @RequestParam String symbol,
            @RequestParam DataType dataType,
            @RequestParam long from,
            @RequestParam long to) {
        FinnhubCandleResponse response = stockDataFetcher.fetchStockData(symbol, from, to);
        Stock savedStock = stockService.saveStockData(symbol, symbol + " Name", response, dataType);
        return ResponseEntity.ok(savedStock);
    }


}