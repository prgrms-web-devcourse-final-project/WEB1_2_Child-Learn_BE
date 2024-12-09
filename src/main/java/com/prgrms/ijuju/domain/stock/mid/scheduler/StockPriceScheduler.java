package com.prgrms.ijuju.domain.stock.mid.scheduler;

import com.prgrms.ijuju.domain.stock.mid.entity.MidStock;
import com.prgrms.ijuju.domain.stock.mid.repository.MidStockRepository;
import com.prgrms.ijuju.domain.stock.mid.service.MidStockChartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StockPriceScheduler {
    private final MidStockChartService midStockChartService;
    private final MidStockRepository midStockRepository;

    @Scheduled(cron = "0 1 0 * * *") // 매일 오전 12시 실행
    public void generateDailyPrices() {
        List<MidStock> stocks = midStockRepository.findAll();
        for (MidStock stock : stocks) {
            midStockChartService.generateDailyPrice(stock);
        }
        log.info("중급 가격 생성 성공");
    }

}
