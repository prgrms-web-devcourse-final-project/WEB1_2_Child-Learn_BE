package com.prgrms.ijuju.domain.stock.mid.repository;

import com.prgrms.ijuju.domain.stock.mid.entity.MidStock;
import com.prgrms.ijuju.domain.stock.mid.entity.MidStockPrice;
import com.prgrms.ijuju.domain.stock.mid.entity.QMidStockPrice;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class MidStockPriceRepositoryImpl implements MidStockPriceRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public MidStockPriceRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<MidStockPrice> findLatestPrice(MidStock stock) {
        QMidStockPrice midStockPrice = QMidStockPrice.midStockPrice;

        return Optional.ofNullable(
                queryFactory
                        .selectFrom(midStockPrice)
                        .where(midStockPrice.midStock.eq(stock))
                        .orderBy(midStockPrice.priceDate.desc())
                        .fetchFirst()
        );
    }

    @Override
    public List<MidStockPrice> find2WeeksPriceInfo(Long midStockId) {
        QMidStockPrice midStockPrice = QMidStockPrice.midStockPrice;

        LocalDateTime endDate = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        LocalDateTime startDate = endDate.minusWeeks(2).withHour(0).withMinute(0).withSecond(0);

        return queryFactory
                .selectFrom(midStockPrice)
                .where(
                        midStockPrice.midStock.id.eq(midStockId),
                        midStockPrice.priceDate.between(startDate, endDate)
                )
                .orderBy(midStockPrice.priceDate.asc())
                .fetch();
    }

    @Override
    public Optional<MidStockPrice> findTodayPrice(Long stockId) {
        QMidStockPrice midStockPrice = QMidStockPrice.midStockPrice;

        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        return Optional.ofNullable(
                queryFactory
                        .selectFrom(midStockPrice)
                        .where(
                                midStockPrice.midStock.id.eq(stockId)
                                        .and(midStockPrice.priceDate.between(startOfDay, endOfDay))
                        )
                        .orderBy(midStockPrice.priceDate.desc())
                        .fetchFirst()
        );
    }

    @Override
    public List<MidStockPrice> findByMidStockId(Long stockId) {
        QMidStockPrice midStockPrice = QMidStockPrice.midStockPrice;

        return queryFactory
                .selectFrom(midStockPrice)
                .where(midStockPrice.midStock.id.eq(stockId))
                .fetch();
    }

    @Override
    public List<MidStockPrice> findFuture2WeeksPriceInfo(Long midStockId) {
        QMidStockPrice midStockPrice = QMidStockPrice.midStockPrice;

        LocalDateTime startDate = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endDate = startDate.plusWeeks(2).withHour(23).withMinute(59).withSecond(59);

        return queryFactory
                .selectFrom(midStockPrice)
                .where(
                        midStockPrice.midStock.id.eq(midStockId),
                        midStockPrice.priceDate.between(startDate, endDate)
                )
                .fetch();
    }

}
