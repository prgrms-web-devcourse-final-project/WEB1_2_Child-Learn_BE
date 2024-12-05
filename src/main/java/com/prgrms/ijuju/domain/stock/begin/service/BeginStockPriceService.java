package com.prgrms.ijuju.domain.stock.begin.service;

import com.prgrms.ijuju.domain.stock.begin.dto.response.BeginStockPriceResponse;
import com.prgrms.ijuju.domain.stock.begin.entity.BeginStockPrice;
import com.prgrms.ijuju.domain.stock.begin.exception.BeginStockErrorCode;
import com.prgrms.ijuju.domain.stock.begin.exception.BeginStockException;
import com.prgrms.ijuju.domain.stock.begin.repository.BeginStockPriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class BeginStockPriceService {
    private static final Random random = new Random();
    private static final double MAX_CHANGE_RATE = 0.3; // 변동폭
    private static final int UNIT = 100;

    private final BeginStockPriceRepository beginStockPriceRepository;

    @Transactional
    public void createWeeklyStockPrice() {
        BeginStockPrice latestStock = beginStockPriceRepository.findTopByOrderByBeginIdDesc()
                .orElseThrow(() -> new BeginStockException(BeginStockErrorCode.STOCK_NOT_FOUND));

        int lastPrice = latestStock.getPrice();
        long recordCount = beginStockPriceRepository.count();
        LocalDate startDate = LocalDate.now().plusDays(1);

        if (isGenerationDate(recordCount, latestStock)) {
            log.info("모의 투자 초급 주식 가격 일주일 데이터 생성");
            List<BeginStockPrice> newWeeklyStocks = generateWeeklyBeginStockData(startDate, lastPrice);
            beginStockPriceRepository.saveAll(newWeeklyStocks);
        }
    }

    private boolean isGenerationDate(long recordCount, BeginStockPrice latestStock) {
        LocalDate lastCreatedDate = latestStock.getStockDate();
        LocalDate today = LocalDate.now();

        return (recordCount == 7 && today.equals(lastCreatedDate.plusDays(3))) ||
                (recordCount > 7 && today.equals(lastCreatedDate.plusDays(7)));
    }

    public List<BeginStockPrice> generateWeeklyBeginStockData(LocalDate startDate, int lastPrice) {
        List<BeginStockPrice> beginStocks = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate date = startDate.plusDays(i);
            int newPrice = generatePrice(lastPrice);
            beginStocks.add(
                    BeginStockPrice.builder()
                            .tradeDay(date.getDayOfWeek()
                                    .getDisplayName(TextStyle.SHORT, Locale.KOREAN))
                            .price(newPrice)
                            .stockDate(date)
                            .build()
            );
        }
        return beginStocks;
    }

    private int generatePrice(int lastPrice) {
        log.info("마지막 주식값 기준 {}의 변동을 주어 새로운 값 생성",MAX_CHANGE_RATE);
        int minPrice = (int)(lastPrice * (1 - MAX_CHANGE_RATE));
        int maxPrice = (int)(lastPrice * (1 + MAX_CHANGE_RATE));

        minPrice = minPrice / 100 * 100;
        maxPrice = maxPrice / 100 * 100;

        int priceUnits = (maxPrice - minPrice) / UNIT;

        return (random.nextInt(priceUnits + 1) * UNIT) + minPrice;
    }

    @Cacheable(value = "stockData", key = "'weeklyPriceData'")
    @Transactional(readOnly = true)
    public List<BeginStockPriceResponse> getBeginStockData() {
        log.info("당일을 기준으로 -3일 ~ +3일의 주식 데이터 조회");

        LocalDate today = LocalDate.now();
        List<BeginStockPrice> weeklyBeginStockData = beginStockPriceRepository.find7BeginStockData(today.minusDays(3), today.plusDays(3));

        return weeklyBeginStockData.stream()
                .map(BeginStockPriceResponse::from)
                .collect(Collectors.toList());
    }
}
