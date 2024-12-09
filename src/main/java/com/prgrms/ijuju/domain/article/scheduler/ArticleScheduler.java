package com.prgrms.ijuju.domain.article.scheduler;

import com.prgrms.ijuju.domain.article.contant.DataType;
import com.prgrms.ijuju.domain.article.data.Trend;
import com.prgrms.ijuju.domain.article.entity.Article;
import com.prgrms.ijuju.domain.article.repository.ArticleRepository;
import com.prgrms.ijuju.domain.article.service.ArticleGenerationService;
import com.prgrms.ijuju.domain.article.service.StockTrendService;
import com.prgrms.ijuju.domain.stock.mid.repository.MidStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ArticleScheduler {

    private static final int MAX_ARTICLES = 5;

    private final ArticleGenerationService articleGenerationService;
    private final ArticleRepository articleRepository;
    private final StockTrendService stockTrendService;
    private final MidStockRepository midStockRepository;


    @Scheduled(cron = "0 30 9 * * ?")
    @Transactional
    public void manageArticles() {

        decrementDurationAndDeleteExpiredArticles();

        manageArticlesByType(DataType.ADVANCED);

        manageArticlesByType(DataType.MID);
    }

    private void decrementDurationAndDeleteExpiredArticles() {
        List<Article> articles = articleRepository.findAll(); // 모든 기사 조회

        articles.forEach(Article::decreaseDuration);

        articleRepository.deleteByDuration(0);
    }


    private void manageArticlesByType(DataType type) {

        long currentCount = articleRepository.countByType(type);

        if (currentCount >= MAX_ARTICLES) {
            return;
        }


        List<Trend> trends = fetchTrendsByType(type);
        articleGenerationService.generateArticles(trends, type);

        removeExcessArticles(type);
    }

    private List<Trend> fetchTrendsByType(DataType type) {
        if (type == DataType.ADVANCED) {
            return stockTrendService.analyzeTrendsForAdvStock();
        } else if (type == DataType.MID) {
            List<Long> midStockIds = midStockRepository.findAllIds();

            return midStockIds.stream()
                    .flatMap(id -> stockTrendService.analyzeTrendsForMidStock(id).stream())
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Unknown DataType: " + type);
        }
    }

    private void removeExcessArticles(DataType type) {
        List<Article> articles = articleRepository.findByType(type);

        if (articles.size() > MAX_ARTICLES) {
            Collections.shuffle(articles);
            List<Article> articlesToRemove = articles.subList(MAX_ARTICLES, articles.size());
            articleRepository.deleteAll(articlesToRemove);
        }
    }


}