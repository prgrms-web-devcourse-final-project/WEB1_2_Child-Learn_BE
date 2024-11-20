package com.prgrms.ijuju.global.init;

import com.prgrms.ijuju.domain.stock.begin.entity.BeginStock;
import com.prgrms.ijuju.domain.stock.begin.generator.BeginStockDataGenerator;
import com.prgrms.ijuju.domain.stock.begin.repository.BeginStockGraphRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("dev")
public class InitBeginStockData {
    private final BeginStockGraphRepository beginStockGraphRepository;
    private final BeginStockDataGenerator beginStockDataGenerator;

    @PostConstruct
    public void init() {
        if (beginStockGraphRepository.count() == 0) {
            LocalDate today = LocalDate.now();
            int initPrice = 500; // 일단 고정

            List<BeginStock> beginStocks = beginStockDataGenerator.generateWeeklyBeginStockData(today, initPrice);
            beginStockGraphRepository.saveAll(beginStocks);
        }
    }
}