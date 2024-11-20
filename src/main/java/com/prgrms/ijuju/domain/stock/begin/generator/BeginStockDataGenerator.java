package com.prgrms.ijuju.domain.stock.begin.generator;

import com.prgrms.ijuju.domain.stock.begin.entity.BeginStock;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Component
public class BeginStockDataGenerator {
    private final Random random = new Random();
    private final double MAX_CHANGE_RATE = 0.3; // 변동폭
    private final int UNIT = 100;

    // baseDate : 그래프 생성 기준 날짜
    public List<BeginStock> generateWeeklyBeginStockData(LocalDate baseDate, int lastPrice) {
        List<BeginStock> beginStocks = new ArrayList<>();
        LocalDate startDate = baseDate.minusDays(3);

        for (int i = 0; i < 7; i++) {
            int minPrice = (int)(lastPrice * (1 - MAX_CHANGE_RATE));
            int maxPrice = (int)(lastPrice * (1 + MAX_CHANGE_RATE));

            minPrice = minPrice / 100 * 100;
            maxPrice = maxPrice / 100 * 100;

            int priceUnits = (maxPrice - minPrice) / UNIT;
            int newPrice = (random.nextInt(priceUnits + 1) * UNIT) + minPrice;

            LocalDate date = startDate.plusDays(i);

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

}
