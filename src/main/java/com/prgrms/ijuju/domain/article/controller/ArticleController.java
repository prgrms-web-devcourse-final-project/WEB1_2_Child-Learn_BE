package com.prgrms.ijuju.domain.article.controller;

import com.prgrms.ijuju.domain.article.dto.request.ArticleRequestDto;
import com.prgrms.ijuju.domain.article.dto.response.ArticleResponseDto;
import com.prgrms.ijuju.domain.article.entity.Article;
import com.prgrms.ijuju.domain.article.scheduler.ArticleScheduler;
import com.prgrms.ijuju.domain.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleScheduler articleScheduler;

    //아티클 자동 생성. 즉 테스트 또는 관리자용
    @PostMapping
    public ResponseEntity<Article> createArticle(@RequestBody ArticleRequestDto request) {
        Article article = Article.builder()
                .stockSymbol(request.getStockSymbol())
                .trendPrediction(request.getTrendPrediction())
                .content(request.getContent())
                .duration(request.getDuration())
                .createdAt(LocalDateTime.now())
                .build();
        Article savedArticle = articleService.saveArticle(article);
        return ResponseEntity.ok(savedArticle);
    }

    //마찬가지로 테스트용입니다. 스케쥴러에 딸려있는 아티클 생성 기능.
    @PostMapping("/start")
    public ResponseEntity<String> manuallyTriggerScheduler() {
        articleScheduler.generateArticles();
        return ResponseEntity.ok("Scheduler triggered successfully.");
    }

    @GetMapping
    public ResponseEntity<List<ArticleResponseDto>> getAllArticles() {
        return ResponseEntity.ok(articleService.getAllArticles());
    }

    @GetMapping("/{stockSymbol}")
    public ResponseEntity<List<ArticleResponseDto>> getArticlesBySymbol(@PathVariable String stockSymbol) {
        return ResponseEntity.ok(articleService.getArticlesBySymbol(stockSymbol));
    }
}