package com.prgrms.ijuju.domain.article.service;

import com.prgrms.ijuju.domain.article.entity.Article;
import com.prgrms.ijuju.domain.article.repository.ArticleRepository;
import com.prgrms.ijuju.domain.article.contant.DataType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public List<Article> findArticlesByType(DataType type) {
        return articleRepository.findByType(type);
    }

    public Article findArticleById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + id));
    }

    public List<Article> findAllArticles() {
        return articleRepository.findAll();
    }
}