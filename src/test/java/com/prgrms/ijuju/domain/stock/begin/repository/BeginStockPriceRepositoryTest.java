package com.prgrms.ijuju.domain.stock.begin.repository;

import com.prgrms.ijuju.domain.stock.begin.entity.BeginStockPrice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@SpringBootTest
@ActiveProfiles("test")
public class BeginStockPriceRepositoryTest {
    @Autowired
    private BeginStockPriceRepository beginStockPriceRepository;

    @DisplayName("마지막으로 등록된 가격과 요일을 조회한다")
    @Test
    void getLastSavedPriceByEntity() {
        // given
        for (int i = 0; i < 7; i++) {
            LocalDate date = LocalDate.now().plusDays(i);
            BeginStockPrice beginStock = BeginStockPrice.builder()
                    .tradeDay(date.getDayOfWeek()
                            .getDisplayName(TextStyle.SHORT, Locale.KOREAN))
                    .price(1000 * i)
                    .stockDate(date)
                    .build();
            beginStockPriceRepository.save(beginStock);
        }

        // when
        BeginStockPrice lastSaved = beginStockPriceRepository.findTopByOrderByBeginIdDesc().orElse(null);

        // then
        assertEquals(6000, lastSaved.getPrice());
        assertEquals(LocalDate.now().plusDays(6), lastSaved.getStockDate());

        String nowDay = LocalDate.now().plusDays(6).getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
        assertEquals(nowDay, lastSaved.getTradeDay());
    }

    @DisplayName("최근 등록순으로 7개의 데이터를 조회한다")
    @Test
    void getLatest7BeginStocks() {
        // given
        LocalDate today = LocalDate.now();
        for (int i = 4; i <= 11; i++) {
            LocalDate date = today.plusDays(i);
            BeginStockPrice beginStock = BeginStockPrice.builder()
                    .tradeDay(date.getDayOfWeek()
                            .getDisplayName(TextStyle.SHORT, Locale.KOREAN))
                    .price(1000 * i)
                    .stockDate(date)
                    .build();
            beginStockPriceRepository.save(beginStock);
        }

        // when
        List<BeginStockPrice> beginStocks = beginStockPriceRepository.find7BeginStockData(today.plusDays(4), today.plusDays(11));

        for (BeginStockPrice beginStock : beginStocks) {
            System.out.println(beginStock.getTradeDay() + " " + beginStock.getStockDate() + " " + beginStock.getPrice());
        }

        // then
        assertEquals(beginStocks.get(0).getPrice(), 4000);

        String nowDay = LocalDate.now().plusDays(4).getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
        assertEquals(beginStocks.get(0).getTradeDay(), nowDay);
    }

}
