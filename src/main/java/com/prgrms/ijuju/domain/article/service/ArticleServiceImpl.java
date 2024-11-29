package com.prgrms.ijuju.domain.article.service;

import com.prgrms.ijuju.domain.article.dto.response.ArticleResponseDto;
import com.prgrms.ijuju.domain.article.entity.Article;
import com.prgrms.ijuju.domain.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    @Override
    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }

    @Override
    public List<ArticleResponseDto> getAllArticles() {
        return articleRepository.findAll()
                .stream()
                .map(ArticleResponseDto::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleResponseDto> getArticlesBySymbol(String stockSymbol) {
        return articleRepository.findByStockSymbol(stockSymbol)
                .stream()
                .map(ArticleResponseDto::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteExpiredArticles() {
        articleRepository.deleteByExpiration(LocalDateTime.now());
    }


}