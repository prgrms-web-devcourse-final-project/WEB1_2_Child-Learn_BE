package com.prgrms.ijuju.domain.article.scheduler;

import com.prgrms.ijuju.domain.article.component.AdvTrendAnalyzerImpl;
import com.prgrms.ijuju.domain.article.data.Trend;
import com.prgrms.ijuju.domain.article.entity.Article;
import com.prgrms.ijuju.domain.article.service.ArticleGenerationService;
import com.prgrms.ijuju.domain.stock.adv.advstock.constant.DataType;
import com.prgrms.ijuju.domain.stock.adv.advstock.entity.AdvStock;
import com.prgrms.ijuju.domain.stock.adv.advstock.repository.AdvStockRepository;
import com.prgrms.ijuju.domain.stock.adv.advstock.service.AdvStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ArticleScheduler {

    private static final int MAX_ARTICLES = 10; // 최대 기사 개수

    private final AdvStockRepository advStockRepository;
    private final AdvTrendAnalyzerImpl trendAnalyzer;
    private final ArticleGenerationService articleGenerationService;

    @Scheduled(cron = "0 30 7 * * ?", zone = "Asia/Seoul")
    public void generateArticles() {
        // 1. 현재 기사 개수 확인
        int currentArticleCount = articleGenerationService.getArticleCount();
        int articlesToGenerate = MAX_ARTICLES - currentArticleCount;

        if (articlesToGenerate <= 0) {
            return; // 추가 생성 필요 없음
        }

        // 2. FORECAST 데이터 가져오기
        List<AdvStock> forecastStocks = advStockRepository.findByDataType(DataType.FORECAST);

        // 3. 트렌드 분석 및 기사 생성
        List<Article> newArticles = generateRandomArticles(forecastStocks, articlesToGenerate);

        // 4. 새 기사 저장
        newArticles.forEach(articleGenerationService::saveArticle);

        // 5. 유지 기간이 지난 기사 삭제
        articleGenerationService.deleteExpiredArticles();
    }

    private List<Article> generateRandomArticles(List<AdvStock> forecastStocks, int articlesToGenerate) {
        // 1. 주식의 모든 트렌드 데이터 가져오기
        List<Trend> allTrends = forecastStocks.stream()
                .flatMap(stock -> trendAnalyzer.analyzeTrends(stock).stream())
                .collect(Collectors.toList());

        // 2. 트렌드 데이터를 랜덤하게 섞고 필요한 개수만큼 선택
        Collections.shuffle(allTrends);
        List<Trend> selectedTrends = allTrends.stream()
                .limit(articlesToGenerate)
                .collect(Collectors.toList());

        // 3. 선택된 트렌드 기준으로 기사 생성
        return selectedTrends.stream()
                .map(trend -> articleGenerationService.createArticleFromTrend(trend))
                .collect(Collectors.toList());
    }
}