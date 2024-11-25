package com.prgrms.ijuju.domain.stock.begin.service;

import com.prgrms.ijuju.domain.stock.begin.dto.request.BeginChatGptMessage;
import com.prgrms.ijuju.domain.stock.begin.dto.request.ChatGptRequest;
import com.prgrms.ijuju.domain.stock.begin.dto.response.BeginStockGraphResponse;
import com.prgrms.ijuju.domain.stock.begin.dto.response.BeginStockQuizResponse;
import com.prgrms.ijuju.domain.stock.begin.dto.response.ChatGptResponse;
import com.prgrms.ijuju.domain.stock.begin.entity.BeginQuiz;
import com.prgrms.ijuju.domain.stock.begin.repository.BeginQuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatGptService {
    private final RestClient chatGptRestClient;
    private final BeginQuizRepository beginQuizRepository;

    private final String MODEL = "gpt-4";
    private final String SYSTEM_ROLE = "system";
    private final String USER_ROLE = "user";
    private final String SYSTEM_CONTENT = """
            너는 주어지는 일봉 단위 일주일의 주식 그래프를 확인하고, 초등학생 고학년 정도가 경제 지식을 기를 수 있는 정도의 OX 문제를 내주는 전문가야.
            문제와 보기는 명확하게 판단이 가능한 문제로 출제를 해주고 무엇보다 비문이 없어야하고, 교육이니까 100% 정답이어야 해. 너는 문제 출제 전문가야!!!
            주식 데이터값은 오늘을 기점으로 -3일부터 +3일까지의 총 7일에 대한 주식 가격 값이 주어질거야.
            문제를 작성한 후에는 반드시 주식 그래프를 참고하여 문제와 지문이 정확한지 확인해. 특히 숫자나 주식 가격이 정확히 일치하는지 반드시 체크해줘.
            문제의 난이도는 초등학생 수준에 맞춰 직관적으로 이해할 수 있는 문제여야 해. 용어를 최대한 단순하게 사용하고, 문제는 30~50자 정도로 작성해.
        """;
    private final double TEMPERATURE = 0.6; // 0일수록 일관되고 예측 가능, 1일수록 창의적이고 다양한 응답

    public List<BeginStockQuizResponse> generateBeginQuiz(List<BeginStockGraphResponse> graphData) {


        // 1. 주식 데이터를 문자열로 변환
        String stockData = graphData.stream()
                .map(stock -> stock.tradeDay() + " : " + stock.price())
                .collect(Collectors.joining("\n"));

        // 2. GPT 요청 생성
        List<BeginChatGptMessage> messages = List.of(
                new BeginChatGptMessage(SYSTEM_ROLE, SYSTEM_CONTENT),
                new BeginChatGptMessage(USER_ROLE, """
                        이 주식 데이터를 보고 O/X 퀴즈를 만들어줘. 정답은 'O'이거나 'X' 한 개만 가능해.
                        응답 형식은 1.{문제 내용}\n2.{'O'에 해당하는 지문}\n3.{'X'에 해당하는 지문}\n4.{정답(O,X)}
                        {}에 해당 내용을 담아서 보내줘. 물론 {} 표기도 없이!
                        문제는 30~50자 내외로 작성해. 반드시 지문에 숫자나 주식 가격이 정확히 일치하는지 확인하고, 비문이 없도록 신경 써줘.
                        """ + stockData
                )
        );

        ChatGptRequest request = new ChatGptRequest(MODEL, messages, TEMPERATURE);

        // 3. GPT API 호출 및 응답 받기
        String response = chatGptRestClient.post()
                .uri("/chat/completions")
                .body(request)
                .retrieve()
                .body(ChatGptResponse.class)
                .choices()
                .get(0)
                .message()
                .content();

        // 4. 응답 파싱
        String[] seperate = response.split("\n");
        String content = seperate[0].substring(3);
        String oContent = seperate[1].substring(3);
        String xContent = seperate[2].substring(3);
        char answer = seperate[3].charAt(3);

        // 5. BeginQuiz 엔티티 생성 및 저장
        BeginQuiz beginQuiz = BeginQuiz.builder()
                .content(content)
                .oContent(oContent)
                .xContent(xContent)
                .answer(answer)
                .build();

        BeginQuiz savedQuiz = beginQuizRepository.save(beginQuiz);

        return List.of(BeginStockQuizResponse.from(savedQuiz));
    }
}
