package com.prgrms.ijuju.domain.stock.begin.service;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.exception.MemberException;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.member.service.MemberService;
import com.prgrms.ijuju.domain.stock.begin.dto.response.BeginStockPriceResponse;
import com.prgrms.ijuju.domain.stock.begin.dto.response.BeginStockQuizResponse;
import com.prgrms.ijuju.domain.stock.begin.dto.response.BeginStockResponse;
import com.prgrms.ijuju.domain.stock.begin.entity.BeginQuiz;
import com.prgrms.ijuju.domain.stock.begin.entity.BeginStockPrice;
import com.prgrms.ijuju.domain.stock.begin.entity.LimitBeginStock;
import com.prgrms.ijuju.domain.stock.begin.exception.BeginStockErrorCode;
import com.prgrms.ijuju.domain.stock.begin.exception.BeginStockException;
import com.prgrms.ijuju.domain.stock.begin.repository.BeginQuizRepository;
import com.prgrms.ijuju.domain.stock.begin.repository.BeginStockPriceRepository;
import com.prgrms.ijuju.domain.stock.begin.repository.LimitBeginStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class BeginStockService {
    private final MemberService memberService;
    private final BeginStockPriceService beginStockPriceService;
    private final MemberRepository memberRepository;
    private final LimitBeginStockRepository limitBeginStockRepository;
    private final BeginQuizRepository beginQuizRepository;

    @Transactional(readOnly = true)
    public BeginStockResponse getBeginStockDataWithQuiz() {
        List<BeginStockPriceResponse> stockData = beginStockPriceService.getBeginStockData();
        List<BeginQuiz> quizResponse = beginQuizRepository.findByCreatedDate(LocalDate.now())
                        .orElseThrow(() -> new BeginStockException(BeginStockErrorCode.QUIZ_NOT_FOUND));

        Random random = new Random();
        int randomIndex = random.nextInt(quizResponse.size());
        List<BeginStockQuizResponse> randomQuizResponse = List.of(BeginStockQuizResponse.from(quizResponse.get(randomIndex)));

        return new BeginStockResponse(stockData, randomQuizResponse);
    }

    public void playBeginStockQuiz(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberException.MEMBER_NOT_FOUND::getMemberTaskException);

        LimitBeginStock limitBeginStock = limitBeginStockRepository.findByMemberId(member.getId())
                        .orElseGet(() -> {
                            LimitBeginStock newLimitBeginStock = new LimitBeginStock(member);
                            limitBeginStockRepository.save(newLimitBeginStock);
                            return newLimitBeginStock;
                        });

        if (limitBeginStock.getLastPlayedDate().equals(LocalDate.now())) {
            throw new BeginStockException(BeginStockErrorCode.ALREADY_PLAYED_GAME);
        }

        limitBeginStock.updateLastPlayedDate();
        limitBeginStockRepository.save(limitBeginStock);
        memberService.increaseBeginStockPlayCount(member);
    }

    @CacheEvict(value = "stockData", key = "'weeklyData'")
    public void refreshStockDataCache() {
        log.info("모의 투자 초급 데이터 캐시 갱신");
    }

}
