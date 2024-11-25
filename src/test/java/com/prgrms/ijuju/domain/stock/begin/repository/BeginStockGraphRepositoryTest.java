package com.prgrms.ijuju.domain.stock.begin.repository;

import com.prgrms.ijuju.domain.stock.begin.entity.BeginStock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@SpringBootTest
@ActiveProfiles("test")
public class BeginStockRepositoryTest {
    @Autowired
    private BeginStockGraphRepository beginStockGraphRepository;

    @DisplayName("엔티티를 통한 최신 가격과 요일을 조회한다")
    @Test
    void getLastSavedPriceByEntity() {
        // given
        for (int i = 0; i < 7; i++) {
            LocalDate date = LocalDate.now().plusDays(i);
            BeginStock beginStock = BeginStock.builder()
                    .tradeDay(date.getDayOfWeek()
                            .getDisplayName(TextStyle.SHORT, Locale.KOREAN))
                    .price(1000 * i)
                    .build();
            beginStockGraphRepository.save(beginStock);
        }

        // when
        BeginStock lastSaved = beginStockGraphRepository.findTopByOrderByBeginIdDesc().orElse(null);

        // then
        Assertions.assertEquals(6000, lastSaved.getPrice());

        String nowDay = LocalDate.now().plusDays(6).getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
        Assertions.assertEquals(nowDay, lastSaved.getTradeDay());
    }

    @DisplayName("최근 등록순으로 7개의 데이터를 조회한다")
    @Test
    void getLatest7BeginStocks() {
        // given
        for (int i = 0; i < 7; i++) {
            LocalDate date = LocalDate.now().plusDays(i);
            BeginStock beginStock = BeginStock.builder()
                    .tradeDay(date.getDayOfWeek()
                            .getDisplayName(TextStyle.SHORT, Locale.KOREAN))
                    .price(1000 * i)
                    .build();
            beginStockGraphRepository.save(beginStock);
        }

        // when
        List<BeginStock> beginStocks = beginStockGraphRepository.findTop7ByOrderByBeginIdDesc();

        // then
        Assertions.assertEquals(beginStocks.get(0).getPrice(), 1000);
        Assertions.assertEquals(beginStocks.get(0).getTradeDay(), LocalDate.now().plusDays(7));
    }

}
