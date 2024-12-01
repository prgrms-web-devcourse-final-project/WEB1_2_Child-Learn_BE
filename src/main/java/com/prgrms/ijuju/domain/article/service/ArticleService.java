package com.prgrms.ijuju.domain.article.service;


import com.prgrms.ijuju.domain.article.dto.ArticleResponseDto;

import java.util.List;

public interface ArticleService {

    List<ArticleResponseDto> getAllArticles();

    List<ArticleResponseDto> getArticlesBySymbol(String stockSymbol);

}