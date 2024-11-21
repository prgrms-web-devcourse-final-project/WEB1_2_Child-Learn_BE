package com.prgrms.ijuju.domain.stock.mid.service;

import com.prgrms.ijuju.domain.stock.mid.dto.response.MidStockPriceResponse;
import com.prgrms.ijuju.domain.stock.mid.dto.response.MidStockResponse;
import com.prgrms.ijuju.domain.stock.mid.dto.response.MidStockWithTradesResponse;
import com.prgrms.ijuju.domain.stock.mid.entity.MidStock;
import com.prgrms.ijuju.domain.stock.mid.entity.MidStockPrice;
import com.prgrms.ijuju.domain.stock.mid.entity.MidStockTrade;
import com.prgrms.ijuju.domain.stock.mid.repository.MidStockPriceRepository;
import com.prgrms.ijuju.domain.stock.mid.repository.MidStockRepository;
import com.prgrms.ijuju.domain.stock.mid.repository.MidStockTradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MidStockService {
    private final MidStockRepository midStockRepository;
    private final MidStockTradeRepository midStockTradeRepository;
    private final MidStockPriceRepository midStockPriceRepository;

    @Transactional(readOnly = true)
    public List<MidStockResponse> findAllStocks() {
        log.info("중급 종목리스트 찾기");
        List<MidStock> stocks = midStockRepository.findAll();
        return stocks.stream()
                .map(MidStockResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MidStockWithTradesResponse> getMemberStocksAndTrades(Long memberId) {
        log.info("중급 보유 주식 조회");
        // 회원의 모든 buy 거래 데이터 조회
        List<MidStockTrade> buyTrades = midStockTradeRepository.findAllBuyMidStock(memberId);

        // 거래 데이터를 종목별로 그룹화
        Map<Long, List<MidStockTrade>> tradeByStockId = buyTrades.stream()
                .collect(Collectors.groupingBy(trade -> trade.getMidStock().getId()));

        // 그룹화된 데이터를 MidStockWithTradesResponse로 변환
        return tradeByStockId.entrySet().stream()
                .map(entry -> {
                    Long stockId = entry.getKey();
                    List<MidStockTrade> trades = entry.getValue();
                    return MidStockWithTradesResponse.of(trades);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MidStockPriceResponse> findStockChartInfo(Long midStockId) {
        log.info("중급 종목 차트 정보 2주치");
        List<MidStockPrice> priceResponses = midStockPriceRepository.find2WeeksPriceInfo(midStockId);
        return priceResponses.stream()
                .map(MidStockPriceResponse::of)
                .collect(Collectors.toList());
    }

}
