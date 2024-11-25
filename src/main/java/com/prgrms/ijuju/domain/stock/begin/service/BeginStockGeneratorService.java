package com.prgrms.ijuju.domain.stock.begin.service;

import com.prgrms.ijuju.domain.stock.begin.entity.BeginStock;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@Component
public class BeginStockDataGenerator {
    private final Random random = new Random();
    private final double MAX_CHANGE_RATE = 0.3; // 변동폭
    private final int UNIT = 100;

    // baseDate : 그래프 생성 기준 날짜
    public List<BeginStock> generateWeeklyBeginStockData(LocalDate startDate, int lastPrice) {
        List<LocalDate> dates = generateWeeklyDates(startDate);
        List<BeginStock> beginStocks = new ArrayList<>();

        for (LocalDate date : dates) {
            int newPrice = generatePrice(lastPrice);
            beginStocks.add(
                    BeginStock.builder()
                            .tradeDay(date.getDayOfWeek()
                                    .getDisplayName(TextStyle.SHORT, Locale.KOREAN))
                            .price(newPrice)
                            .build()
            );
        }
        return beginStocks;
    }

    private List<LocalDate> generateWeeklyDates(LocalDate startDate) {
        List<LocalDate> dates = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            dates.add(startDate.plusDays(i));
        }
        Collections.reverse(dates);
        return dates;
    }

    private int generatePrice(int lastPrice) {
        int minPrice = (int)(lastPrice * (1 - MAX_CHANGE_RATE));
        int maxPrice = (int)(lastPrice * (1 + MAX_CHANGE_RATE));

        minPrice = minPrice / 100 * 100;
        maxPrice = maxPrice / 100 * 100;

        int priceUnits = (maxPrice - minPrice) / UNIT;

        return (random.nextInt(priceUnits + 1) * UNIT) + minPrice;
    }
}
