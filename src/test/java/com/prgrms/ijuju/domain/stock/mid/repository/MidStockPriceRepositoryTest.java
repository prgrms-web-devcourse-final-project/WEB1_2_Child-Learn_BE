package com.prgrms.ijuju.domain.stock.mid.repository;

import com.prgrms.ijuju.domain.stock.mid.entity.MidStock;
import com.prgrms.ijuju.domain.stock.mid.entity.MidStockPrice;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MidStockPriceRepositoryTest {
    @Autowired
    private MidStockPriceRepository midStockPriceRepository;

    @Autowired
    private MidStockRepository midStockRepository;

    private MidStock stock;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        // 테스트용 주식 데이터 생성
        stock = new MidStock("테스트주식");
        midStockRepository.save(stock);
        now = LocalDateTime.now();
    }

    @Test
    @DisplayName("특정 주식의 가격 정보를 조회한다")
    void findByMidStockId() {
        // given
        MidStockPrice price = MidStockPrice.builder()
                .midStock(stock)
                .highPrice(1000L)
                .lowPrice(900L)
                .avgPrice(950L)
                .priceDate(now)
                .build();
        midStockPriceRepository.save(price);

        // when
        List<MidStockPrice> prices = midStockPriceRepository.findByMidStockId(stock.getId());

        // then
        assertThat(prices).hasSize(1);
        MidStockPrice foundPrice = prices.get(0);
        assertThat(foundPrice.getHighPrice()).isEqualTo(1000L);
        assertThat(foundPrice.getLowPrice()).isEqualTo(900L);
        assertThat(foundPrice.getAvgPrice()).isEqualTo(950L);
    }

    @Test
    @DisplayName("오래된 가격 데이터를 삭제한다")
    @Rollback
    void deleteOldData() {
        // given
        LocalDateTime oldDate = now.minusDays(31);
        MidStockPrice oldPrice = MidStockPrice.builder()
                .midStock(stock)
                .highPrice(1000L)
                .lowPrice(900L)
                .avgPrice(950L)
                .priceDate(oldDate)
                .build();
        midStockPriceRepository.save(oldPrice);

        // when
        midStockPriceRepository.deleteOldData(now.minusDays(30));

        // then
        List<MidStockPrice> remainingPrices = midStockPriceRepository.findAll();
        assertThat(remainingPrices).isEmpty();
    }

    @Test
    @DisplayName("최신 가격 정보를 조회한다")
    void findLatestPrice() {
        // given
        LocalDateTime yesterday = now.minusDays(1);
        MidStockPrice oldPrice = MidStockPrice.builder()
                .midStock(stock)
                .highPrice(1000L)
                .lowPrice(900L)
                .avgPrice(950L)
                .priceDate(yesterday)
                .build();
        midStockPriceRepository.save(oldPrice);

        MidStockPrice newPrice = MidStockPrice.builder()
                .midStock(stock)
                .highPrice(1100L)
                .lowPrice(1000L)
                .avgPrice(1050L)
                .priceDate(now)
                .build();
        midStockPriceRepository.save(newPrice);

        // when
        Optional<MidStockPrice> latestPrice = midStockPriceRepository.findLatestPrice(stock);

        // then
        assertThat(latestPrice).isPresent();
        assertThat(latestPrice.get().getAvgPrice()).isEqualTo(1050L);
        assertThat(latestPrice.get().getPriceDate()).isEqualTo(now);
    }

//    @Test
//    @DisplayName("가격 이력을 페이징하여 조회한다")
//    void findPriceHistory() {
//        // given
//        List<MidStockPrice> prices = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            prices.add(MidStockPrice.builder()
//                    .midStock(stock)
//                    .highPrice(1000L + i * 100)
//                    .lowPrice(900L + i * 100)
//                    .avgPrice(950L + i * 100)
//                    .priceDate(now.minusDays(i))
//                    .build());
//        }
//        midStockPriceRepository.saveAll(prices);
//
//        // when
//        List<MidStockPrice> foundPrices = midStockPriceRepository.findPriceHistory(
//                stock,
//                PageRequest.of(0, 3)
//        );
//
//        // then
//        assertThat(foundPrices).hasSize(3);
//        assertThat(foundPrices.get(0).getPriceDate())
//                .isAfter(foundPrices.get(1).getPriceDate());
//    }

    @Test
    @DisplayName("특정 주식의 2주치 과거 데이터를 조회한다")
    void find2WeeksPriceInfo() {
        // given
        MidStock stock = new MidStock("테스트주식");
        midStockRepository.save(stock);

        // 20일치 데이터 생성
        for (int i = 0; i < 20; i++) {
            MidStockPrice price = MidStockPrice.builder()
                    .midStock(stock)
                    .highPrice(1000L + (i * 100))
                    .lowPrice(900L + (i * 100))
                    .avgPrice(950L + (i * 100))
                    .priceDate(LocalDateTime.now().minusDays(i))
                    .build();
            midStockPriceRepository.save(price);
        }

        // when
        List<MidStockPrice> prices = midStockPriceRepository.find2WeeksPriceInfo(stock.getId());

        // then
        assertThat(prices).hasSize(14); // 2주치 데이터만 조회되는지 확인

        // 날짜 순서대로 정렬되었는지 확인
        for (int i = 0; i < prices.size() - 1; i++) {
            assertThat(prices.get(i).getHighPrice()).isEqualTo(2900L - (i * 100));
        }
    }
}