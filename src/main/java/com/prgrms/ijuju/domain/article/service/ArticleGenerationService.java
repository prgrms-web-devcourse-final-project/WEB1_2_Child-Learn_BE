package com.prgrms.ijuju.domain.article.service;

import com.prgrms.ijuju.domain.article.contant.DataType;
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
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleGenerationService {

    private final RestClient chatGptRestClient;
    private final ArticleRepository articleRepository;

    private static final String MODEL = "gpt-3.5-turbo";
    private static final String SYSTEM_ROLE = "system";
    private static final double TEMPERATURE = 0.7;

    private static final List<String> KEYWORDS = List.of(
            "신제품 출시", "환경 보호 정책 발표", "기술 혁신 발표", "대규모 고용 발표", "공장 가동 중단",
            "자연재해", "국제 무역 협정 체결", "원자재 가격 상승", "원자재 가격 하락", "정부 보조금 지급",
            "신규 시장 진출", "주요 계약 체결", "핵심 인물 퇴임", "회사의 이미지 스캔들", "국가 간 갈등 심화",
            "혁신적인 기술 발표", "에너지 위기", "신약 개발 성공", "산업 파업 발생", "인공지능 기술 확산",
            "전기차 판매 증가", "지속 가능한 에너지 투자", "우주 탐사 성공 발표", "대규모 투자 유치",
            "회사의 지배 구조 변화", "이사회 충돌", "특허 소송", "대형 기업 인수합병", "새로운 규제 발표",
            "소비자 행동 변화", "주요 지표 발표", "중요 인수인계", "유명 기업의 파산", "부동산 시장 변화",
            "해외 투자 증가", "자율주행차 기술 발전", "중앙은행 금리 인하", "중앙은행 금리 인상", "대규모 리콜 발표",
            "수출입 데이터 변동", "주요 도시에서의 시위", "회사의 신뢰도 하락", "국제 유가 변동", "에너지 절약 캠페인",
            "클린 에너지 발전", "주요 금융 기관의 예측", "농산물 가격 상승", "농산물 가격 하락", "코로나 재확산",
            "헬스케어 기술 개발"
    );

    private String getRandomKeyword() {
        return KEYWORDS.get(ThreadLocalRandom.current().nextInt(KEYWORDS.size()));
    }

    public void generateArticles(List<Trend> trends, DataType type) {
        String randomKeyword = getRandomKeyword();

        for (Trend trend : trends) {
            try {
                Article article = createArticleFromTrend(trend, type, randomKeyword);
                articleRepository.save(article);
            } catch (Exception e) {
                log.error("Failed to generate article for trend: {}", trend, e);
            }
        }
    }

    public Article createArticleFromTrend(Trend trend, DataType type, String keyword) {
        String trendDescription = formatTrendData(trend);

        String response = gptResponse(trendDescription, keyword);

        return parseGptResponse(response, trend, type);
    }

    private String gptResponse(String trendData, String keyword) {

        List<Map<String, Object>> messages = List.of(
                Map.of("role", SYSTEM_ROLE, "content", getSystemContent(keyword)),
                Map.of("role", "user", "content", "다음 트렌드를 기반으로 기사를 작성해줘:\n" + trendData)
        );


        Map<String, Object> request = Map.of(
                "model", MODEL,
                "messages", messages,
                "temperature", TEMPERATURE
        );


        Map<String, Object> response = chatGptRestClient.post()
                .uri("/chat/completions")
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});


        @SuppressWarnings("unchecked")
        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
        if (choices == null || choices.isEmpty()) {
            throw new IllegalStateException("GPT 응답에 choices 데이터가 없습니다.");
        }

        Map<String, Object> firstChoice = choices.get(0);
        Map<String, String> message = (Map<String, String>) firstChoice.get("message");

        return message.get("content");
    }

    private String formatTrendData(Trend trend) {
        return String.format("Symbol: %s, Type: %s, Description: %s",
                trend.getStockSymbol(), trend.getDurationType(), trend.getDescription());
    }

    private String getSystemContent(String keyword) {
        return """
            너는 주어진 주식 트렌드 데이터를 기반으로 초등학생 고학년이 이해할 수 있는 경제 기사를 작성하는 전문가야.
            주식 트렌드 데이터를 바탕으로 재미있고 간단한 설명을 포함한 기사를 작성해줘.
            상승과 하락에 대해서 대놓고 알려주지 말고,("~일 동안 상승 예정입니다" 이런거 하면 안된다는 말).
            상승이나 하락이 생길만한 사건에 대한 기사로 만들어 주면 돼.
            이번 기사는 '%s'와 관련된 애용을 포함해야 해. 배경과 맥락, 사건등은 랜덤하게 만들어줘.
            기사는 초등학생도 쉽게 이해할 수 있는 내용으로 작성되어야 해. 다양하게 만들어야 하니 주제는 랜덤으로 해줘.
        """.formatted(keyword);
    }



    private Article parseGptResponse(String response, Trend trend, DataType type) {
        try {
            return Article.builder()
                    .stockSymbol(trend.getStockSymbol())
                    .trendPrediction(trend.getDurationType())
                    .content(response)
                    .duration(determineDuration(trend.getDurationType()))
                    .createdAt(LocalDateTime.now())
                    .type(type)
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
}