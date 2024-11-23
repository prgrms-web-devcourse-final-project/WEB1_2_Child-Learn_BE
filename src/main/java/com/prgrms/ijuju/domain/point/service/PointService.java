package com.prgrms.ijuju.domain.point.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.point.dto.response.PointDetailsResponseDTO;
import com.prgrms.ijuju.domain.point.dto.response.PointResponseDTO;
import com.prgrms.ijuju.domain.point.entity.Coin;
import com.prgrms.ijuju.domain.point.entity.CoinDetails;
import com.prgrms.ijuju.domain.point.entity.GameType;
import com.prgrms.ijuju.domain.point.entity.Point;
import com.prgrms.ijuju.domain.point.entity.PointDetails;
import com.prgrms.ijuju.domain.point.entity.PointStatus;
import com.prgrms.ijuju.domain.point.entity.PointType;
import com.prgrms.ijuju.domain.point.entity.StockType;
import com.prgrms.ijuju.domain.point.repository.CoinRepository;
import com.prgrms.ijuju.domain.point.repository.PointDetailsRepository;
import com.prgrms.ijuju.domain.point.repository.PointRepository;
import com.prgrms.ijuju.domain.point.repository.CoinDetailsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PointService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private PointDetailsRepository pointDetailsRepository;

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private CoinDetailsRepository coinDetailsRepository;

    // 현재 잔여 포인트와 코인 조회
    public PointResponseDTO currentPointAndCoin(Long memberId) {
        Point point = getOrCreatePoint(memberId);
        Coin coin = getOrCreateCoin(memberId);
        
        // 포인트와 코인 동기화
        synchronizePointsAndCoins(point, coin);

        List<PointDetailsResponseDTO> history = pointDetailsRepository.findByMemberId(memberId)
                .stream()
                .map(pointDetails -> new PointDetailsResponseDTO(
                    pointDetails.getId(),
                    pointDetails.getPointType(),
                    pointDetails.getPointAmount(), 
                    pointDetails.getPointStatus(),
                    pointDetails.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return new PointResponseDTO(point.getCurrentPoints(), coin.getCurrentCoins(), history);
    }

    // 포인트 동기화
    private void synchronizePointsAndCoins(Point point, Coin coin) {
        if (point.getCurrentPoints() <= 0) {
            point.setCurrentPoints(0L);
        }
        if (coin.getCurrentCoins() <= 0) {
            coin.setCurrentCoins(0L);
        }
        pointRepository.save(point);
        coinRepository.save(coin);
    }

    // 포인트 조회 및 생성
    private Point getOrCreatePoint(Long memberId) {
        Optional<Point> optionalPoint = pointRepository.findByMemberId(memberId);
        if (optionalPoint.isPresent()) {
            Point point = optionalPoint.get();
            return point;
        } else {
            throw new NoSuchElementException("Point not found for memberId: " + memberId);
        }
    }

    // 코인 조회 및 생성
    private Coin getOrCreateCoin(Long memberId) {
        return coinRepository.findByMemberId(memberId)
                .orElse(Coin.builder()
                        .member(memberRepository.findById(memberId).orElseThrow())
                        .currentCoins(0L)
                        .updatedAt(LocalDateTime.now())
                        .build());
    }

    // 포인트 획득 - 미니게임
    @Transactional
    public void earnPoints(Long memberId, Long pointsEarned, GameType gameType) {
        Point point = getOrCreatePoint(memberId);
        point.setCurrentPoints(point.getCurrentPoints() + pointsEarned);
        pointRepository.save(point);

        PointDetails pointDetails = createPointDetails(point, pointsEarned, gameType.name(), PointStatus.EARNED);
        pointDetailsRepository.save(pointDetails);
    }

    // 포인트 투자 및 획득
    @Transactional
    public void investPoints(Long memberId, Long pointsToInvest, StockType stockType, Long pointsEarned) {
        Point point = getOrCreatePoint(memberId);
        if (point.getCurrentPoints() < pointsToInvest) {
            throw new RuntimeException("투자할 포인트가 충분하지 않습니다.");
        }
        
        // 포인트 투자
        point.setCurrentPoints(point.getCurrentPoints() - pointsToInvest);
        pointRepository.save(point);
        PointDetails investDetails = createPointDetails(point, pointsToInvest, stockType.name(), PointStatus.USED);
        pointDetailsRepository.save(investDetails);

        // 포인트 획득
        if (pointsEarned > 0) {
            point.setCurrentPoints(point.getCurrentPoints() + pointsEarned);
            pointRepository.save(point);
            PointDetails earnDetails = createPointDetails(point, pointsEarned, stockType.name(), PointStatus.EARNED);
            pointDetailsRepository.save(earnDetails);
        }
    }

    // 포인트 환전
    @Transactional
    public void exchangePointToCoin(Long memberId, Long pointsToExchange) {
        Point point = getOrCreatePoint(memberId);
        if (pointsToExchange < 100 || point.getCurrentPoints() < pointsToExchange) {
            throw new RuntimeException("최소 환전 금액은 100입니다.");
        }
        point.setCurrentPoints(point.getCurrentPoints() - pointsToExchange);
        pointRepository.save(point);

        Coin coin = getOrCreateCoin(memberId);
        Long coinsReceived = pointsToExchange / 100;
        coin.setCurrentCoins(coin.getCurrentCoins() + coinsReceived);
        coinRepository.save(coin);

        // 코인 환전 내역 기록
        CoinDetails coinDetails = new CoinDetails();
        coinDetails.setCoin(coin);
        coinDetails.setMember(coin.getMember());
        coinDetails.setCoinAmount(coinsReceived);
        coinDetails.setCreatedAt(LocalDateTime.now());
        coinDetailsRepository.save(coinDetails);
    }

    // 포인트 기록 조회 및 필터링
    public List<PointDetailsResponseDTO> getPointHistory(Long memberId, PointType pointType, PointStatus pointStatus) {
        return pointDetailsRepository.findByDetails(memberId, pointType, pointStatus)
                .stream()
                .map(detail -> new PointDetailsResponseDTO(
                    detail.getId(),
                    detail.getPointType(),
                    detail.getPointAmount(),
                    detail.getPointStatus(),
                    detail.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    // 포인트 기록 생성 
    private PointDetails createPointDetails(Point point, Long pointAmount, String pointType, PointStatus pointStatus) {
        PointDetails pointDetails = new PointDetails();
        pointDetails.setPoint(point);
        pointDetails.setMember(point.getMember());
        pointDetails.setPointType(PointType.valueOf(pointType));
        pointDetails.setPointAmount(pointAmount);
        pointDetails.setPointStatus(pointStatus);
        pointDetails.setCreatedAt(LocalDateTime.now());
        return pointDetails;
    }
    
    // 포인트 기록 조회 및 필터링
    public List<PointDetailsResponseDTO> getPointHistory(Long memberId, PointType pointType, PointStatus pointStatus, LocalDateTime startDate, LocalDateTime endDate) {
        if (pointType == null && pointStatus == null && startDate == null && endDate == null) {
            // 전체 기록 조회
            return pointDetailsRepository.findByMemberId(memberId)
                    .stream()
                    .map(PointDetailsResponseDTO::from)
                    .collect(Collectors.toList());
        } else if (startDate != null && endDate != null) {
            // 주간별 필터링
            return pointDetailsRepository.findByDate(memberId, startDate, endDate)
                    .stream() 
                    .map(PointDetailsResponseDTO::from)
                    .collect(Collectors.toList());
        } else {
            // pointType, pointStatus 필터링
            return pointDetailsRepository.findByDetails(memberId, pointType, pointStatus)
                    .stream()
                    .map(PointDetailsResponseDTO::from)
                    .collect(Collectors.toList());
        }
    }

    // 코인 사용
    // public void useCoin(Long memberId, Long coinsToUse) {
    //     Coin coin = getOrCreateCoin(memberId);
    //     if (coin.getCurrentCoins() < coinsToUse) {
    //         throw new RuntimeException("사용할 코인이 충분하지 않습니다.");
    //     }
    //     coin.setCurrentCoins(coin.getCurrentCoins() - coinsToUse);
    //     coinRepository.save(coin);

    //     // 코인 사용 내역 기록
    //     CoinDetails coinDetails = new CoinDetails();
    //     coinDetails.setCoin(coin);
    //     coinDetails.setMember(coin.getMember());
    //     coinDetails.setCoinAmount(coinsToUse);
    //     coinDetails.setCreatedAt(LocalDateTime.now());
    //     coinDetailsRepository.save(coinDetails);
    // }
} 