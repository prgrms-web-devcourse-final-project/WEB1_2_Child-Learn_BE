package com.prgrms.ijuju.domain.article.controller;

import com.prgrms.ijuju.domain.article.scheduler.ArticleScheduler;
import com.prgrms.ijuju.domain.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.prgrms.ijuju.domain.article.entity.Article;
import com.prgrms.ijuju.domain.article.contant.DataType;




@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleScheduler articleScheduler;


    @GetMapping("/{type}")
    public ResponseEntity<List<Article>> getArticlesByType(@PathVariable DataType type) {
        List<Article> articles = articleService.findArticlesByType(type);
        return ResponseEntity.ok(articles);
    }


    @GetMapping("/detail/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id) {
        Article article = articleService.findArticleById(id);
        return ResponseEntity.ok(article);
    }

    //디버깅 용도
    @GetMapping
    public ResponseEntity<List<Article>> getAllArticles() {
        List<Article> articles = articleService.findAllArticles();
        return ResponseEntity.ok(articles);
    }

    //디버깅 용도
    @PostMapping("/manage-articles")
    public ResponseEntity<String> executeManageArticlesScheduler() {
        articleScheduler.manageArticles();
        return ResponseEntity.ok("스케쥴러 기능이 강제로 실행되었습니다.");
    }
}
