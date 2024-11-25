package com.prgrms.ijuju.domain.stock.begin.service;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.exception.MemberException;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.member.service.MemberService;
import com.prgrms.ijuju.domain.stock.begin.dto.response.BeginStockGraphResponse;
import com.prgrms.ijuju.domain.stock.begin.dto.response.BeginStockQuizResponse;
import com.prgrms.ijuju.domain.stock.begin.dto.response.BeginStockResponse;
import com.prgrms.ijuju.domain.stock.begin.entity.BeginStockGraph;
import com.prgrms.ijuju.domain.stock.begin.entity.LimitBeginStock;
import com.prgrms.ijuju.domain.stock.begin.exception.BeginStockErrorCode;
import com.prgrms.ijuju.domain.stock.begin.exception.BeginStockException;
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
    private final MemberRepository memberRepository;
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

    public void playBeginStockQuiz(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberException.MEMBER_NOT_FOUND::getMemberTaskException);

        LimitBeginStock limitBeginStock = limitBeginStockRepository.findByMemberId(member.getId())
                .orElse(new LimitBeginStock(member));

        if (limitBeginStock.getLastPlayedDate().equals(LocalDate.now())) {
            throw new BeginStockException(BeginStockErrorCode.ALREADY_PLAYED_GAME);
        }

        limitBeginStock.updateLastPlayedDate();
        limitBeginStockRepository.save(limitBeginStock);
        memberService.increaseBeginStockPlayCount(member);
    }

}
