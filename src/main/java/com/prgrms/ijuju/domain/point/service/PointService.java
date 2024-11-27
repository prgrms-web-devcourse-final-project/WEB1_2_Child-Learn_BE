package com.prgrms.ijuju.domain.point.service;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.point.dto.request.PointRequestDTO;
import com.prgrms.ijuju.domain.point.dto.response.PointResponseDTO;
import com.prgrms.ijuju.domain.point.entity.PointDetails;
import com.prgrms.ijuju.domain.point.entity.ExchangeDetails;
import com.prgrms.ijuju.domain.point.repository.PointDetailsRepository;
import com.prgrms.ijuju.domain.point.repository.ExchangeRepository;
import com.prgrms.ijuju.domain.point.entity.GameType;
import com.prgrms.ijuju.domain.point.entity.PointType;
import com.prgrms.ijuju.domain.point.entity.StockStatus;
import com.prgrms.ijuju.domain.point.entity.PointStatus;
import com.prgrms.ijuju.domain.point.entity.StockType;
import com.prgrms.ijuju.domain.point.exception.PointException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class PointService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PointDetailsRepository pointDetailsRepository;

    @Autowired
    private ExchangeRepository exchangeRepository;

    // 현재 포인트와 코인 조회
    @Transactional(readOnly = true)
    public PointResponseDTO CurrentPointsAndCoins(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException(PointException.MEMBER_NOT_FOUND.getMessage()));
        return new PointResponseDTO(member.getPoints(), member.getCoins());
    }

    // 미니게임 결과 처리
    public PointResponseDTO playMiniGame(Long memberId, GameType gameType, Long earnedPoints, boolean isWin) {
        
        // 회원 조회
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException(PointException.MEMBER_NOT_FOUND.getMessage()));

        // 현재 포인트 조회
        Long currentPoints = member.getPoints();
       
        // 요청받은 포인트를 더해줌
        Long updatedPoints = currentPoints + earnedPoints;
        
        // 포인트 업데이트
        member.setPoints(updatedPoints);
        memberRepository.save(member);
        
        // 포인트 거래 내역 기록
        PointDetails pointDetails = PointDetails.builder()
            .member(member)
            .pointType(PointType.GAME)
            .pointAmount(earnedPoints)
            .pointStatus(PointStatus.EARNED)
            .detailType(gameType.name())
            .createdAt(LocalDateTime.now())
            .build();
        pointDetailsRepository.save(pointDetails);
        
        // 업데이트된 포인트로 응답 반환
        return new PointResponseDTO(updatedPoints, member.getCoins());
    }

    // 주식 투자 결과 처리
    public PointResponseDTO simulateStockInvestment(PointRequestDTO request, StockType stockType, StockStatus stockStatus) {
        
        Long amount = request.getPointAmount();

        // 유효하지 않은 금액 처리
        if (amount <= 0) {
            throw new RuntimeException(PointException.INVALID_AMOUNT.getMessage());
        }

        // 회원 조회
        Member member = memberRepository.findById(request.getMemberId())
            .orElseThrow(() -> new RuntimeException(PointException.MEMBER_NOT_FOUND.getMessage()));

        // 현재 포인트 조회
        Long currentPoints = member.getPoints();

        // 주식 투자 처리
        PointStatus pointStatus;
        if (stockStatus == StockStatus.BUY) {
            if (currentPoints < amount) {
                throw new RuntimeException(PointException.INSUFFICIENT_POINTS.getMessage());
            }
            currentPoints -= amount;
            pointStatus = PointStatus.USED; // BUY일 때 USED로 설정
        } else if (stockStatus == StockStatus.SELL) {
            currentPoints += amount;
            pointStatus = PointStatus.EARNED; // SELL일 때 EARNED로 설정
        } else {
            throw new RuntimeException(PointException.INVALID_STOCK_STATUS.getMessage());
        }

        // 포인트 업데이트
        member.setPoints(currentPoints);
        memberRepository.save(member);

        // 포인트 거래 내역 기록
        PointDetails pointDetails = PointDetails.builder()
            .member(member)
            .pointType(PointType.STOCK)
            .pointAmount(amount)
            .pointStatus(pointStatus)
            .createdAt(LocalDateTime.now())
            .detailType(stockType.name())
            .build();
        pointDetailsRepository.save(pointDetails);

        // 업데이트된 포인트로 응답 반환
        return new PointResponseDTO(currentPoints, member.getCoins());
    }

    // 포인트를 코인으로 환전
    public PointResponseDTO exchangePointsToCoins(Long memberId, Long pointsToExchange) {

        // 최소 100포인트부터 환전 가능
        if (pointsToExchange < 100) {
            throw new RuntimeException(PointException.EXCHANGE_MINIMUM.getMessage());
        }
        if (pointsToExchange % 100 != 0) {
            throw new RuntimeException(PointException.EXCHANGE_UNIT.getMessage());
        }

        // 회원 조회
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException(PointException.MEMBER_NOT_FOUND.getMessage()));

        // 현재 포인트에서 차감
        Long currentPoints = member.getPoints();
        if (currentPoints < pointsToExchange) {
            throw new RuntimeException(PointException.INSUFFICIENT_POINTS.getMessage());
        }
        currentPoints -= pointsToExchange;
        member.setPoints(currentPoints);

        // 코인 증가
        Long coinsToAdd = pointsToExchange / 100;
        member.setCoins(member.getCoins() + coinsToAdd);
        memberRepository.save(member);

        // 포인트 거래 내역 기록
        PointDetails pointDetails = PointDetails.builder()
            .member(member)
            .pointType(PointType.EXCHANGE)
            .pointAmount(pointsToExchange)
            .pointStatus(PointStatus.USED)
            .createdAt(LocalDateTime.now())
            .build();
        pointDetailsRepository.save(pointDetails);

        // 환전 내역 기록
        ExchangeDetails exchangeDetails = ExchangeDetails.builder()
            .member(member)
            .pointsExchanged(pointsToExchange)
            .coinsReceived(coinsToAdd)
            .createdAt(LocalDateTime.now())
            .build();
        exchangeRepository.save(exchangeDetails);

        // 업데이트된 포인트와 코인으로 응답 반환
        return new PointResponseDTO(member.getPoints(), member.getCoins());
    }
    
    // 포인트 거래 내역 조회 (포인트타입/포인트상태/주간별 필터링)
    @Transactional(readOnly = true)
    public List<PointDetails> getFilteredPointHistory(Long memberId, PointType type, PointStatus status, Integer weekOfYear) {
        LocalDateTime startDate;
        LocalDateTime endDate;

        if (weekOfYear != null) {
            startDate = LocalDateTime.now().with(WeekFields.of(Locale.getDefault()).weekOfYear(), weekOfYear).with(DayOfWeek.MONDAY);
            endDate = startDate.plusDays(6);
        } else {
            startDate = LocalDateTime.MIN;
            endDate = LocalDateTime.MAX;
        }

        return pointDetailsRepository.findByMemberIdAndCreatedAtBetweenAndPointType(memberId, startDate, endDate, type);
    }

    // 포인트에서 코인으로 환전된 거래내역 조회
    @Transactional(readOnly = true)
    public List<ExchangeDetails> getExchangeHistory(Long memberId) {
        return exchangeRepository.findByMemberId(memberId);
    }
}
