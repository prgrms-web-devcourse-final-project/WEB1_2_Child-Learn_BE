package com.prgrms.ijuju.domain.article.scheduler;

import com.prgrms.ijuju.domain.article.service.ArticleService;
import com.prgrms.ijuju.domain.stock.adv.advstock.constant.DataType;
import com.prgrms.ijuju.domain.stock.adv.advstock.entity.AdvStock;
import com.prgrms.ijuju.domain.stock.adv.advstock.service.AdvStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ArticleScheduler {

    private final AdvStockService advStockService;
    private final TrendAnalyzer trendAnalyzer;
    private final ChatGptArticleService chatGptArticleService;
    private final ArticleService articleService;

    @Scheduled(cron = "0 30 7 * * ?", zone = "Asia/Seoul")
    public void generateArticles() {
        List<AdvStock> forecastStocks = advStockService.getStocksByDataType(DataType.FORECAST);

        for (AdvStock stock : forecastStocks) {
            try {
                // 1. 트렌드 분석
                String trend = trendAnalyzer.analyzeTrend(stock.getClosePrices());

                // 2. 기사 생성
                String content = chatGptArticleService.generateArticle(stock.getSymbol(), trend, stock.getClosePrices());

                // 3. 기사 저장
                articleService.saveArticle(stock.getSymbol(), trend, content, 3); // 유지 기간 3일
            } catch (Exception e) {
                System.err.println("Error processing stock: " + stock.getSymbol() + " - " + e.getMessage());
            }
        }

        // 4. 유지 기간이 지난 기사 삭제
        articleService.deleteExpiredArticles();
    }
}