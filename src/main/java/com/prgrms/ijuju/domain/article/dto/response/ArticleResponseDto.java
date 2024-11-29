package com.prgrms.ijuju.domain.article.dto.response;

import com.prgrms.ijuju.domain.article.entity.Article;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleResponseDto {
    private String stockSymbol;
    private String trendPrediction;
    private String content;
    private LocalDateTime createdAt;


    public static ArticleResponseDto toResponse(Article article) {
        return ArticleResponseDto.builder()
                .stockSymbol(article.getStockSymbol())
                .trendPrediction(article.getTrendPrediction())
                .content(article.getContent())
                .createdAt(article.getCreatedAt())
                .build();
    }
}

