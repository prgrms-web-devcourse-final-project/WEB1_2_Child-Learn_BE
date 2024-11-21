package com.prgrms.ijuju.domain.stock.begin.init;

import com.prgrms.ijuju.domain.stock.begin.entity.BeginStock;
import com.prgrms.ijuju.domain.stock.begin.generator.BeginStockDataGenerator;
import com.prgrms.ijuju.domain.stock.begin.repository.BeginStockGraphRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitBeginStockData {
    private final BeginStockGraphRepository beginStockGraphRepository;
    private final BeginStockDataGenerator beginStockDataGenerator;

    @PostConstruct
    public void init() {
        if (beginStockGraphRepository.count() == 0) {
            LocalDate startDate = LocalDate.now().minusDays(3);
            int initPrice = 500; // 일단 고정

            List<BeginStock> beginStocks = beginStockDataGenerator.generateWeeklyBeginStockData(startDate, initPrice);
            beginStockGraphRepository.saveAll(beginStocks);
        }
    }
}
