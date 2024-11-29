package com.prgrms.ijuju.domain.article.service;

import com.prgrms.ijuju.domain.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleGenerationService {

    private final RestClient chatGptRestClient; // RestClient 주입
    private final ArticleRepository articleRepository;

    private static final String MODEL = "gpt-3.5-turbo";
    private static final String SYSTEM_ROLE = "system";
    private static final String USER_ROLE = "user";
    private static final String SYSTEM_CONTENT = """
        너는 주어진 주식 데이터를 기반으로 초등학생 고학년이 이해할 수 있는 경제 기사를 작성하는 전문가야.
        주식 데이터의 트렌드를 분석하고, 간단하고 재미있게 설명해줘. 
        기사의 내용은 주식 트렌드와 간단한 경제 용어를 포함해야 해.
    """;

    private static final double TEMPERATURE = 0.7;

    public void generateArticle() {
        // 주식 데이터 준비 (데이터 준비 방식은 Article 로직에 맞게 구현)
        List<ArticleStockData> stockData = getArticleStockData();

        String formattedStockData = formatStockData(stockData);

        String response = gptResponse(formattedStockData);

        Article article = parseGptResponse(response);
        articleRepository.save(article);
    }

    private String gptResponse(String stockData) {
        List<ChatGptMessage> messages = List.of(
                new ChatGptMessage(SYSTEM_ROLE, SYSTEM_CONTENT),
                new ChatGptMessage(USER_ROLE, "다음 데이터를 기반으로 기사를 작성해줘:\n" + stockData)
        );

        ChatGptRequest request = new ChatGptRequest(MODEL, messages, TEMPERATURE);

        return chatGptRestClient.post()
                .uri("/chat/completions")
                .body(request)
                .retrieve()
                .body(ChatGptResponse.class)
                .choices()
                .get(0)
                .message()
                .content();
    }

    private String formatStockData(List<ArticleStockData> stockData) {
        return stockData.stream()
                .map(data -> data.getDate() + ": " + data.getPrice())
                .collect(Collectors.joining("\n"));
    }

    private Article parseGptResponse(String response) {
        try {
            // GPT 응답 파싱 로직 (Article 엔티티에 맞게 구현)
            return Article.builder()
                    .content(response)
                    .build();
        } catch (Exception e) {
            log.error("GPT 응답 파싱 실패: {}", response, e);
            throw new IllegalArgumentException("GPT 응답 파싱 에러 발생: " + response, e);
        }
    }

    private List<ArticleStockData> getArticleStockData() {
        // 주식 데이터 가져오기 로직 구현
        return List.of(
                new ArticleStockData("2023-11-28", 150.50),
                new ArticleStockData("2023-11-29", 152.30)
        );
    }
}