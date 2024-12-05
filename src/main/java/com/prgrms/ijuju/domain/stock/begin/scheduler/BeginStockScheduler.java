package com.prgrms.ijuju.domain.stock.begin.scheduler;

import com.prgrms.ijuju.domain.stock.begin.service.BeginStockGptService;
import com.prgrms.ijuju.domain.stock.begin.service.BeginStockPriceService;
import com.prgrms.ijuju.domain.stock.begin.service.BeginStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Component
public class BeginStockScheduler {
    private final BeginStockPriceService beginStockPriceService;
    private final BeginStockGptService beginStockGptService;
    private final BeginStockService beginStockService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void createWeeklyStockData() {
        beginStockPriceService.createWeeklyStockPrice();

        for (int i = 0; i < 5; i++) {
            beginStockGptService.generateBeginQuiz();
        }

        beginStockService.refreshStockDataCache();
    }
}
