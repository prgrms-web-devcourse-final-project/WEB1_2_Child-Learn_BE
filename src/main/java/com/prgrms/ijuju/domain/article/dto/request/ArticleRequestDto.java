package com.prgrms.ijuju.domain.article.dto.request;

import lombok.Getter;

@Getter
public class ArticleRequestDto {
    private String stockSymbol;
    private String trendPrediction;
    private String content;
    private int duration;
}
