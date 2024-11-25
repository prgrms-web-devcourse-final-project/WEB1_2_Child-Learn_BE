package com.prgrms.ijuju.domain.stock.begin.scheduler;

import com.prgrms.ijuju.domain.stock.begin.entity.BeginStockGraph;
import com.prgrms.ijuju.domain.stock.begin.exception.BeginStockErrorCode;
import com.prgrms.ijuju.domain.stock.begin.exception.BeginStockException;
import com.prgrms.ijuju.domain.stock.begin.service.BeginStockGeneratorService;
import com.prgrms.ijuju.domain.stock.begin.repository.BeginStockGraphRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Component
public class BeginStockScheduler {
    private final BeginStockGraphRepository beginStockGraphRepository;
    private final BeginStockGeneratorService beginStockDataGenerator;

    @Scheduled(cron = "0 0 0 * * ?")
    public void createWeeklyStockData() {
        log.info("초급 주식 일주일 데이터 생성");

        BeginStockGraph latestStock = beginStockGraphRepository.findTopByOrderByBeginIdDesc()
                .orElseThrow(() -> new BeginStockException(BeginStockErrorCode.STOCK_NOT_FOUND));

        int lastPrice = latestStock.getPrice();
        long recordCount = beginStockGraphRepository.count();
        LocalDate startDate = LocalDate.now().plusDays(1);

        if (isGenerationDate(recordCount, latestStock)) {
            List<BeginStockGraph> newWeeklyStocks = beginStockDataGenerator.generateWeeklyBeginStockData(startDate, lastPrice);
            beginStockGraphRepository.saveAll(newWeeklyStocks);
        }
    }

    private boolean isGenerationDate(long recordCount, BeginStockGraph latestStock) {
        LocalDate lastCreatedDate = latestStock.getStockDate();
        LocalDate today = LocalDate.now();

        return (recordCount == 7 && today.equals(lastCreatedDate.plusDays(3))) ||
                (recordCount > 7 && today.equals(lastCreatedDate.plusDays(7)));
    }

}
