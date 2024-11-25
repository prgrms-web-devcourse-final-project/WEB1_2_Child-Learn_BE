package com.prgrms.ijuju.domain.stock.begin.service;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.service.MemberService;
import com.prgrms.ijuju.domain.stock.begin.dto.response.BeginStockGraphResponse;
import com.prgrms.ijuju.domain.stock.begin.dto.response.BeginStockQuizResponse;
import com.prgrms.ijuju.domain.stock.begin.dto.response.BeginStockResponse;
import com.prgrms.ijuju.domain.stock.begin.entity.BeginStockGraph;
import com.prgrms.ijuju.domain.stock.begin.entity.LimitBeginStock;
import com.prgrms.ijuju.domain.stock.begin.repository.BeginStockGraphRepository;
import com.prgrms.ijuju.domain.stock.begin.repository.LimitBeginStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class BeginStockService {
    private final ChatGptService chatGptService;
    private final MemberService memberService;
    private final BeginStockGraphRepository beginStockGraphRepository;
    private final LimitBeginStockRepository limitBeginStockRepository;

    @Transactional(readOnly = true)
    public List<BeginStockGraphResponse> getBeginStockData() {
        log.info("당일을 기준으로 -3일 ~ +3일의 주식 데이터 조회");

        LocalDate today = LocalDate.now();
        List<BeginStockGraph> weeklyBeginStockData = beginStockGraphRepository.find7BeginStockData(today.minusDays(3), today.plusDays(3));

        return weeklyBeginStockData.stream()
                .map(BeginStockGraphResponse::from)
                .collect(Collectors.toList());
    }

    // 주식 데이터를 Gpt에게 넘겨주고 최종 응답 생성
    @Transactional(readOnly = true)
    public BeginStockResponse getBeginStockDataWithQuiz() {
        List<BeginStockGraphResponse> stockData = getBeginStockData();
        List<BeginStockQuizResponse> quizResponse = chatGptService.generateBeginQuiz(stockData);

        return new BeginStockResponse(stockData, quizResponse);
    }

    public LimitBeginStock getOrCreateLimitBeginStock(Long memberId) {
        log.info("사용자 id를 통해 마지막 게임 시간 신규 생성 및 갱신");

        Member member = memberService.getMemberByMemberId(memberId);
        return limitBeginStockRepository.findByMemberId(memberId)
                .orElseGet(() -> limitBeginStockRepository.save( new LimitBeginStock(member)));
    }

    public void updateBeginQuiz(Long memberId) {
        log.info("사용자의 초급 모의투자 플레이 횟수 +1");

        LimitBeginStock limitBeginStock = getOrCreateLimitBeginStock(memberId);
        memberService.updateBeginStockPlayCount(limitBeginStock.getPlayer());
    }

}
