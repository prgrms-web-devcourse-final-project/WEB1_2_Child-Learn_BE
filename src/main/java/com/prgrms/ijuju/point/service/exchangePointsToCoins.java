package com.prgrms.ijuju.point.service;

import com.prgrms.ijuju.point.entity.Coin;
import com.prgrms.ijuju.point.entity.CoinDetails;

import java.time.LocalDateTime;

@Transactional
public void exchangePointsToCoins(Long memberId, Long pointsToExchange) {
    // 포인트 조회
    Point point = pointRepository.findByMemberId(memberId)
            .orElseThrow(() -> new RuntimeException("User not found"));

    if (point.getCurrentPoints() < pointsToExchange) {
        throw new RuntimeException("Not enough points to exchange.");
    }

    // 포인트 업데이트
    point.setCurrentPoints(point.getCurrentPoints() - pointsToExchange);
    pointRepository.save(point);

    // 코인 조회
    Coin coin = coinRepository.findByMemberId(memberId)
            .orElseThrow(() -> new RuntimeException("User not found"));
    Long coinsToAdd = pointsToExchange / 100;
    coin.setCurrentCoins(coin.getCurrentCoins() + coinsToAdd);
    coinRepository.save(coin);

    // 코인 상세 정보 저장
    CoinDetails coinDetails = new CoinDetails();
    coinDetails.setCoin(coin);
    coinDetails.setMemberId(memberId);
    coinDetails.setCoinAmount(coinsToAdd);
    coinDetails.setCreatedAt(LocalDateTime.now());
    coinDetailsRepository.save(coinDetails);
}
