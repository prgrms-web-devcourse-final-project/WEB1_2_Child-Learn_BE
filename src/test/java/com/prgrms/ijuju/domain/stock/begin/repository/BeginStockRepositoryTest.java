package com.prgrms.ijuju.domain.stock.begin.repository;

import com.prgrms.ijuju.domain.stock.begin.entity.BeginStock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class BeginStockRepositoryTest {
    @Autowired
    private BeginStockGraphRepository beginStockGraphRepository;

    @DisplayName("DB에 저장된 마지막 가격 값을 beginID 내림차순을 통해 가져온다.")
    @Test
    public void getLastSavedPrice() {
        BeginStock lastSaveBeginStock = BeginStock.builder()
                .tradeDay("월")
                .price(1000)
                .build();

        beginStockGraphRepository.save(lastSaveBeginStock);

        int lastSavePrice = beginStockGraphRepository.findPriceByOrderByBeginIdDesc().orElse(500);

        Assertions.assertEquals(1000, lastSaveBeginStock.getPrice());
    }
}
