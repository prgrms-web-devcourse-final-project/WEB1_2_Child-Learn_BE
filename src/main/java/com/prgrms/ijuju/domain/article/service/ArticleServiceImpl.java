package com.prgrms.ijuju.domain.article.service;

import com.prgrms.ijuju.domain.article.dto.ArticleResponseDto;
import com.prgrms.ijuju.domain.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

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

}