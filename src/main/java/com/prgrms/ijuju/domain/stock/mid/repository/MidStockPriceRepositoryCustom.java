package com.prgrms.ijuju.domain.stock.mid.repository;

import com.prgrms.ijuju.domain.stock.mid.entity.MidStock;
import com.prgrms.ijuju.domain.stock.mid.entity.MidStockPrice;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MidStockPriceRepositoryCustom {
    Optional<MidStockPrice> findLatestPrice(MidStock stock);
    List<MidStockPrice> find2WeeksPriceInfo(Long midStockId);
    Optional<MidStockPrice> findTodayPrice(Long stockId);
    List<MidStockPrice> findByMidStockId(Long stockId);
//    List<MidStockPrice> findPriceHistory(MidStock stock, Pageable pageable);
}
