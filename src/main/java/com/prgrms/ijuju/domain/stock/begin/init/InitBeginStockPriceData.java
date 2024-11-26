package com.prgrms.ijuju.domain.stock.begin.init;

import com.prgrms.ijuju.domain.stock.begin.entity.BeginStockPrice;
import com.prgrms.ijuju.domain.stock.begin.repository.BeginQuizRepository;
import com.prgrms.ijuju.domain.stock.begin.service.BeginStockGptService;
import com.prgrms.ijuju.domain.stock.begin.service.BeginStockPriceService;
import com.prgrms.ijuju.domain.stock.begin.repository.BeginStockPriceRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitBeginStockPriceData {
    private final BeginStockPriceRepository beginStockPriceRepository;
    private final BeginQuizRepository beginQuizRepository;
    private final BeginStockPriceService beginStockPriceService;
    private final BeginStockGptService beginStockGptService;

    @PostConstruct
    @Transactional
    public void init() {
        if (beginStockPriceRepository.count() == 0) {
            LocalDate startDate = LocalDate.now().minusDays(3);
            int initPrice = 500; // 일단 고정

            List<BeginStockPrice> beginStocks = beginStockPriceService.generateWeeklyBeginStockData(startDate, initPrice);
            beginStockPriceRepository.saveAll(beginStocks);
        }

        if (beginQuizRepository.count() == 0) {
            beginStockGptService.generateBeginQuiz();
        }
    }
}
