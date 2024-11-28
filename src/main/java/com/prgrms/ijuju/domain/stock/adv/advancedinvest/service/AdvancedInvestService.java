package com.prgrms.ijuju.domain.stock.adv.advancedinvest.service;


import com.prgrms.ijuju.domain.stock.adv.advancedinvest.dto.request.StockTransactionRequestDto;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

public interface AdvancedInvestService {

    // 게임 타이머 시작
    void startGameTimer(WebSocketSession session, Long gameId, int startSecond);

    // 게임 시작
    void startGame(WebSocketSession session, Long memberId);

    // 게임 일시정지
    void pauseGame(Long gameId, int second);

    // 게임 재개
    void resumeGame(WebSocketSession session, Long gameId);

    // 게임 종료
    void endGame(Long gameId);

    // 남은 시간 조회
    int getRemainingTime(Long gameId);

    // 7시 리셋
    void resetPlayedTodayStatus();

    // 주식 구매
    void buyStock(Long gameId, StockTransactionRequestDto request);

    // 주식 판매
    void sellStock(Long gameId, StockTransactionRequestDto request);


}
