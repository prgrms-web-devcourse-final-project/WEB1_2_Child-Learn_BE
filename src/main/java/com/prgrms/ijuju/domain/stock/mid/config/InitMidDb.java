package com.prgrms.ijuju.domain.stock.mid.config;

import com.prgrms.ijuju.domain.stock.mid.entity.MidStock;
import com.prgrms.ijuju.domain.stock.mid.entity.MidStockPrice;
import com.prgrms.ijuju.domain.stock.mid.repository.MidStockPriceRepository;
import com.prgrms.ijuju.domain.stock.mid.repository.MidStockRepository;
import com.prgrms.ijuju.domain.stock.mid.service.MidStockChartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitMidDb {
    private static final int DAYS_TO_KEEP = 15;

    private final MidStockRepository midStockRepository;
    private final MidStockPriceRepository midStockPriceRepository;
    public final MidStockChartService midStockChartService;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void init() {
        // 데이터가 이미 존재하는지 확인
        if (isDataExists()) {
            log.info("중급 초기 데이터 이미 존재");
            return;  // 데이터가 있으면 초기화 건너뛰기
        }
        log.info("중급 초기 데이터 생성");
        List<MidStock> stocks = createInitialStocks();
        midStockRepository.saveAll(stocks);

        for (MidStock stock : stocks) {
            List<MidStockPrice> prices = createInitialPrices(stock);
            midStockPriceRepository.saveAll(prices);
        }
    }

    private List<MidStock> createInitialStocks() {
        return Arrays.asList(
                new MidStock("삼성전자"),
                new MidStock("현대자동차"),
                new MidStock("카카오")
        );
    }

    private List<MidStockPrice> createInitialPrices(MidStock stock) {
        List<MidStockPrice> prices = new ArrayList<>();

        // 초기 기준가격 설정
        long basePrice;
        if (stock.getId() % 3 == 1) {
            basePrice = 100L;
        } else if (stock.getId() % 3 == 2) {
            basePrice = 10000L;
        } else {
            basePrice = 1000000L;
        }

        long lastAvgPrice = basePrice;


        // 30일치 데이터 생성 (15일 전부터 15일 후까지)
        for (int i = -DAYS_TO_KEEP; i <= DAYS_TO_KEEP; i++) {
            MidStockPrice price = midStockChartService.generatePrice(stock, lastAvgPrice, LocalDateTime.now().plusDays(i));
            prices.add(price);
            lastAvgPrice = price.getAvgPrice();
        }

        return prices;
    }

    private boolean isDataExists() {
        return midStockRepository.count() > 0;
    }
}
