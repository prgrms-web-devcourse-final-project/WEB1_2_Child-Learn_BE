package com.prgrms.ijuju.domain.stock.begin.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.ijuju.domain.stock.begin.dto.request.BeginChatGptMessage;
import com.prgrms.ijuju.domain.stock.begin.dto.request.ChatGptRequest;
import com.prgrms.ijuju.domain.stock.begin.dto.response.ChatGptResponse;
import com.prgrms.ijuju.domain.stock.begin.entity.BeginQuiz;
import com.prgrms.ijuju.domain.stock.begin.entity.BeginStockPrice;
import com.prgrms.ijuju.domain.stock.begin.repository.BeginQuizRepository;
import com.prgrms.ijuju.domain.stock.begin.repository.BeginStockPriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class BeginStockGptService {
    private final RestClient chatGptRestClient;
    private final BeginQuizRepository beginQuizRepository;
    private final BeginStockPriceRepository beginStockPriceRepository;

    private static final String MODEL = "gpt-3.5-turbo";
    private static final String SYSTEM_ROLE = "system";
    private static final String USER_ROLE = "user";
    private static final double TEMPERATURE = 0.6;

    private static final List<String> QUIZ_TOPICS = List.of(
            "가격의 움직임 파악하기", "최고가와 최저가 찾기", "어제와 오늘의 가격 비교",
            "가격 상승과 하락의 횟수", "요일별 가격 차이 계산",
            "특정 가격대 확인하기", "가격 변화의 크기 비교", "주식 가격의 상승과 하락",
            "가격 상승의 원리", "가격 하락의 이유", "장기 투자의 이해",
            "투자자 심리", "시장 반응", "매수 시점", "매도 시점"
    );

    public void generateBeginQuiz() {
        LocalDate today = LocalDate.now();
        List<BeginStockPrice> priceData = beginStockPriceRepository.find7BeginStockData(
                today.minusDays(3),
                today.plusDays(3)
        );

        String stockData = priceData.stream()
                .map(stock -> String.format("%s(%s) : %d", stock.getStockDate(), stock.getTradeDay(), stock.getPrice()))
                .collect(Collectors.joining("\n"));

        String quizTopic = QUIZ_TOPICS.get((int) (Math.random() * QUIZ_TOPICS.size()));
        log.info("모의 투자 초급 주제: {}", quizTopic);

        String response = gptResponse(stockData, quizTopic);
        BeginQuiz quiz = parseGptResponse(response);
        beginQuizRepository.save(quiz);

        log.info("{}의 모의 투자 초급 퀴즈 생성", LocalDate.now());
    }

    private String gptResponse(String stockData, String quizTopic) {
        List<BeginChatGptMessage> messages = List.of(
                new BeginChatGptMessage(SYSTEM_ROLE, String.format("""
                        너는 초등학생이 이해할 수 있는 수준의 '%s'와 관련된 OX 퀴즈를 만들어주는 전문가야.
                        아래 데이터는 7일간의 주식 가격이며, 날짜(요일)과 가격 데이터로 구성되어 있어.
                        문제는 반드시 명확한 O 또는 X로 판별 가능한 정답이 있어야 하고, 초등학생이 이해할 수 있도록 간단한 문장으로 작성해줘.
                        답변에는 어제, 오늘같은 단어 요일로만 표기해줘. 연속적인 데이터에 대한 지문은 출제하지 않아.
                        퀴즈는 JSON 형식으로 아래와 같은 구조로 작성해줘.
                        O와 X에 해당하는 지문인지 한 번 더 체크해줘.:
                        {
                            "quiz": {
                                "topic": "주제",
                                "question": "문제 내용",
                                "oContent": "O에 해당하는 지문(O, 이런식으로 표현하지 말고 구체적으로 표현)",
                                "xContent": "X에 해당하는 지문(X, 이런식으로 표현하지 말고 구체적으로 표현)",
                                "answer": "정답 (O 또는 X인 한글자로)"
                            }
                        }
                        """, quizTopic)),
                new BeginChatGptMessage(USER_ROLE, String.format("""
                        주어진 데이터를 보고 초등학생이 이해할 수 있는 수준의 '%s'와 관련된 O/X 퀴즈를 만들어줘.
                        데이터는 요일별 주식 가격을 나타내고 있어. 문제는 30~50자 내외로 작성해줘.
                        반드시 지켜야 할 사항:
                            1. 문제는 제시된 날짜와 가격 데이터만을 사용해 판단 가능해야 함
                            2. 가격 데이터는 오늘을 기준으로 -3일 ~ +3일인 7일간의 데이터임
                            3. 어제, 오늘같은 단어 대신 요일로만 표현
                            4. 날짜나 요일을 언급할 때는 정확한 요일을 사용
                            5. 답변의 근거가 데이터에서 명확히 확인 가능해야 함
                        다음은 가격 데이터야: %s
                        """, QUIZ_TOPICS, stockData))
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

    private BeginQuiz parseGptResponse(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response);

            JsonNode quiz = root.path("quiz");
            String content = quiz.path("question").asText();
            String oContent = quiz.path("oContent").asText();
            String xContent = quiz.path("xContent").asText();
            String answer = quiz.path("answer").asText();

            content = content.replaceFirst("^\\d+\\.", "").trim();
            oContent = oContent.replaceFirst("^\\d+\\.", "").trim();
            xContent = xContent.replaceFirst("^\\d+\\.", "").trim();
            answer = answer.replaceFirst("^\\d+\\.", "").trim();

            return BeginQuiz.builder()
                    .content(content)
                    .oContent(oContent)
                    .xContent(xContent)
                    .answer(answer)
                    .build();
        } catch (Exception e) {
            log.error("GPT 응답 파싱 실패: {}", response, e);
            throw new IllegalArgumentException("GPT 응답 파싱 에러 발생: " + response, e);
        }
    }
}
