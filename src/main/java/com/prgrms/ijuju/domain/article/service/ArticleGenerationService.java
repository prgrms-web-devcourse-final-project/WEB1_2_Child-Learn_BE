package com.prgrms.ijuju.domain.article.service;

import com.prgrms.ijuju.domain.article.data.Trend;
import com.prgrms.ijuju.domain.article.entity.Article;
import com.prgrms.ijuju.domain.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleGenerationService {

    private final RestClient chatGptRestClient;
    private final ArticleRepository articleRepository;

    private static final String MODEL = "gpt-3.5-turbo";
    private static final String SYSTEM_ROLE = "system";
    private static final double TEMPERATURE = 0.7;

    public Article createArticleFromTrend(Trend trend) {
        String trendDescription = formatTrendData(trend);

        String response = gptResponse(trendDescription);

        return parseGptResponse(response, trend);
    }

    private String gptResponse(String trendData) {
        // 메시지 구성
        List<Map<String, Object>> messages = List.of(
                Map.of("role", SYSTEM_ROLE, "content", getSystemContent()),
                Map.of("role", "user", "content", "다음 트렌드를 기반으로 기사를 작성해줘:\n" + trendData)
        );

        // 요청 생성
        Map<String, Object> request = Map.of(
                "model", MODEL,
                "messages", messages,
                "temperature", TEMPERATURE
        );

        // JSON 응답 처리
        Map<String, Object> response = chatGptRestClient.post()
                .uri("/chat/completions")
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});

        // "choices" 배열에서 첫 번째 메시지의 "content" 추출
        @SuppressWarnings("unchecked") // 강제 캐스팅 경고 억제
        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
        if (choices == null || choices.isEmpty()) {
            throw new IllegalStateException("GPT 응답에 choices 데이터가 없습니다.");
        }

        Map<String, Object> firstChoice = choices.get(0);
        Map<String, String> message = (Map<String, String>) firstChoice.get("message");

        return message.get("content"); // "content" 반환
    }


    private String formatTrendData(Trend trend) {
        return String.format("Symbol: %s, Type: %s, Description: %s",
                trend.getStockSymbol(), trend.getDurationType(), trend.getDescription());
    }

    private String getSystemContent() {
        return """
            너는 주어진 주식 트렌드 데이터를 기반으로 초등학생 고학년이 이해할 수 있는 경제 기사를 작성하는 전문가야.
            주식 트렌드 데이터를 바탕으로 재미있고 간단한 설명을 포함한 기사를 작성해줘.
            기사는 초등학생도 쉽게 이해할 수 있는 내용으로 작성되어야 해.
        """;
    }

    private Article parseGptResponse(String response, Trend trend) {
        try {
            return Article.builder()
                    .stockSymbol(trend.getStockSymbol()) // Trend의 주식 심볼 사용
                    .trendPrediction(trend.getDurationType()) // Trend의 유형 사용
                    .content(response) // GPT로 생성된 기사 내용
                    .duration(determineDuration(trend.getDurationType())) // 유형에 따른 지속 기간 설정
                    .createdAt(LocalDateTime.now()) // 생성 시간 설정
                    .build();
        } catch (Exception e) {
            log.error("GPT 응답 파싱 실패: {}", response, e);
            throw new IllegalArgumentException("GPT 응답 파싱 에러 발생: " + response, e);
        }
    }

    private int determineDuration(String trendType) {
        return switch (trendType) {
            case "SHORT_TERM" -> 3;
            case "MID_TERM" -> 7;
            case "LONG_TERM" -> 14;
            default -> throw new IllegalArgumentException("알 수 없는 트렌드 타입: " + trendType);
        };
    }

    public int getArticleCount() {
        return (int) articleRepository.count();
    }

    public void saveArticle(Article article) {
        articleRepository.save(article);
    }

    public void deleteExpiredArticles() {
        articleRepository.deleteByExpiration(LocalDateTime.now());
    }
}