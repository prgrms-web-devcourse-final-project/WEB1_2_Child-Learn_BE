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
@RequestMapping("api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleScheduler articleScheduler;

    // 모든 기사 조회
    @GetMapping
    public ResponseEntity<List<ArticleResponseDto>> getAllArticles() {
        List<ArticleResponseDto> articles = articleService.getAllArticles();
        return ResponseEntity.ok(articles);
    }

    // 특정 주식 심볼로 기사 조회
    @GetMapping("/{stockSymbol}")
    public ResponseEntity<List<ArticleResponseDto>> getArticlesBySymbol(@PathVariable String stockSymbol) {
        List<ArticleResponseDto> articles = articleService.getArticlesBySymbol(stockSymbol);
        return ResponseEntity.ok(articles);
    }

    // 스케줄러 강제 실행
    @PostMapping("/scheduler/run")
    public ResponseEntity<String> runScheduler() {
        articleScheduler.generateArticles();
        return ResponseEntity.ok("Article Scheduler 강제 실행 완료.");
    }
}