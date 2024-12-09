package com.prgrms.ijuju.domain.article.service;

import com.prgrms.ijuju.domain.article.component.AdvTrendAnalyzer;
import com.prgrms.ijuju.domain.article.component.MidTrendAnalyzer;
import com.prgrms.ijuju.domain.article.data.Trend;
import com.prgrms.ijuju.domain.article.exception.ArticleErrorCode;
import com.prgrms.ijuju.domain.article.exception.ArticleNotFoundException;
import com.prgrms.ijuju.domain.stock.adv.advstock.constant.DataType;
import com.prgrms.ijuju.domain.stock.adv.advstock.entity.AdvStock;
import com.prgrms.ijuju.domain.stock.adv.advstock.repository.AdvStockRepository;
import com.prgrms.ijuju.domain.stock.mid.dto.response.MidStockResponse;
import com.prgrms.ijuju.domain.stock.mid.entity.MidStock;
import com.prgrms.ijuju.domain.stock.mid.entity.MidStockPrice;
import com.prgrms.ijuju.domain.stock.mid.repository.MidStockPriceRepository;
import com.prgrms.ijuju.domain.stock.mid.repository.MidStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Adv, Mid 데이터를 각각 받고 Trend를 계산하여 List<Trend> 로 return 하는 클래스 입니다
 */

@Service
@RequiredArgsConstructor
public class StockTrendService {

    private final AdvStockRepository advStockRepository;
    private final AdvTrendAnalyzer advTrendAnalyzer;

    private final MidStockRepository midStockRepository;
    private final MidStockPriceRepository midStockPriceRepository;
    private final MidTrendAnalyzer midTrendAnalyzer;


    @Transactional(readOnly = true)
    public List<Trend> analyzeTrendsForAdvStock() {
        List<AdvStock> forecastStocks = advStockRepository.findByDataType(DataType.FORECAST);

        return forecastStocks.stream()
                .flatMap(stock -> advTrendAnalyzer.analyzeTrends(stock).stream())
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<Trend> analyzeTrendsForMidStock(Long midStockId) {

        MidStock midStock = midStockRepository.findById(midStockId)
                .orElseThrow(() -> new ArticleNotFoundException(ArticleErrorCode.NO_TRENDS_FOUND));

        MidStockResponse midStockResponse = MidStockResponse.of(midStock);

        List<MidStockPrice> prices = midStockPriceRepository.findFuture2WeeksPriceInfo(midStockId);

        // 전체 데이터 기반으로 트렌드 분석
        return midTrendAnalyzer.analyzeTrends(prices, midStockResponse.midName());
    }
}
