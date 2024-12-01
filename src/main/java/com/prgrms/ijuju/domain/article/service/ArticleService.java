package com.prgrms.ijuju.domain.article.service;


import com.prgrms.ijuju.domain.article.dto.response.ArticleResponseDto;
import com.prgrms.ijuju.domain.article.entity.Article;

import java.util.List;

public interface ArticleService {

    List<ArticleResponseDto> getAllArticles();

    List<ArticleResponseDto> getArticlesBySymbol(String stockSymbol);

}