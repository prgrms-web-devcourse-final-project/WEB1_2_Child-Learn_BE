package com.prgrms.ijuju.domain.stock.begin.service;

import com.prgrms.ijuju.domain.stock.begin.entity.BeginStockGraph;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@Slf4j
@Component
public class BeginStockGeneratorService {
    private final Random random = new Random();
    private final double MAX_CHANGE_RATE = 0.3; // 변동폭
    private final int UNIT = 100;

    // baseDate : 그래프 생성 기준 날짜
    public List<BeginStockGraph> generateWeeklyBeginStockData(LocalDate startDate, int lastPrice) {
        List<BeginStockGraph> beginStocks = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate date = startDate.plusDays(i);
            int newPrice = generatePrice(lastPrice);
            beginStocks.add(
                    BeginStockGraph.builder()
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
}
