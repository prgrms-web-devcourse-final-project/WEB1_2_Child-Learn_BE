package com.prgrms.ijuju.domain.stock.mid.service;

import com.prgrms.ijuju.domain.stock.mid.entity.MidStock;
import com.prgrms.ijuju.domain.stock.mid.entity.MidStockPrice;
import com.prgrms.ijuju.domain.stock.mid.repository.MidStockPriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MidStockChartService {
    private static final int DAYS_TO_KEEP = 15; // 사실상 30일치 데이터를 유지
    private static final double PRICE_CHANGE_RATE = 0.05;
    private final Random random = new Random();
    private final MidStockPriceRepository midStockPriceRepository;

    public void generateDailyPrice(MidStock midStock) {
        log.info("중급 하루 한번 가격 생성");

        LocalDateTime twoWeekDaysAgo = LocalDateTime.now().minusDays(DAYS_TO_KEEP);
        midStockPriceRepository.deleteOldData(twoWeekDaysAgo);

        long lastAvgPrice = midStockPriceRepository.findLatestPrice(midStock)
                .map(MidStockPrice::getAvgPrice)
                .orElse(getBaseMinPrice(midStock) * 10);

        LocalDateTime now = LocalDateTime.now().plusDays(DAYS_TO_KEEP);
        MidStockPrice newPrice = generatePrice(midStock, lastAvgPrice, now);

        midStockPriceRepository.save(newPrice);
    }

    public MidStockPrice generatePrice(MidStock stock, long lastAvgPrice, LocalDateTime date) {
        long baseMinPrice;

        baseMinPrice = getBaseMinPrice(stock);

        while (true) {
            // 최소 가격 보장
            long minLowPrice = Math.max(baseMinPrice, (long)(lastAvgPrice * 0.8));
            long maxLowPrice = (long)(lastAvgPrice * 1.2);

            // 저가 계산 (최소 가격 이상 보장)
            long lowPrice = minLowPrice + (long)((maxLowPrice - minLowPrice) * random.nextDouble());

            // 고가 계산 (저가보다 항상 높게)
            long minHighPrice = lowPrice + 1; // 최소한 저가보다 1원 이상
            long maxHighPrice = (long)(lowPrice * 1.2);
            long highPrice = minHighPrice + (long)((maxHighPrice - minHighPrice) * random.nextDouble());

            // 평균가격 계산
            long avgPrice = (lowPrice + highPrice) / 2;

            // 가격 변동폭 체크
            if (Math.abs(avgPrice - lastAvgPrice) <= lastAvgPrice * PRICE_CHANGE_RATE) {
                return MidStockPrice.builder()
                        .lowPrice(lowPrice)
                        .highPrice(highPrice)
                        .avgPrice(avgPrice)
                        .priceDate(date)
                        .midStock(stock)
                        .build();
            }
        }
    }

    private static long getBaseMinPrice(MidStock stock) {
        long basePrice;
        if (stock.getId() % 3 == 1) {
            basePrice = 10L;
        } else if (stock.getId() % 3 == 2) {
            basePrice = 500L;
        } else {
            basePrice = 10000L;
        }
        return basePrice;
    }
}