package com.prgrms.ijuju.domain.wallet.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.wallet.dto.request.ExchangeRequestDTO;
import com.prgrms.ijuju.domain.wallet.dto.request.GamePointRequestDTO;
import com.prgrms.ijuju.domain.wallet.dto.request.StockPointRequestDTO;
import com.prgrms.ijuju.domain.wallet.dto.request.AttendanceRequestDTO;
import com.prgrms.ijuju.domain.wallet.dto.response.WalletResponseDTO;
import com.prgrms.ijuju.domain.wallet.entity.ExchangeTransaction;
import com.prgrms.ijuju.domain.wallet.entity.PointTransaction;
import com.prgrms.ijuju.domain.wallet.entity.PointType;
import com.prgrms.ijuju.domain.wallet.entity.TransactionType;
import com.prgrms.ijuju.domain.wallet.entity.Wallet;
import com.prgrms.ijuju.domain.wallet.exception.WalletException;
import com.prgrms.ijuju.domain.wallet.repository.PointTransactionRepository;
import com.prgrms.ijuju.domain.wallet.repository.WalletRepository;
import com.prgrms.ijuju.domain.wallet.repository.ExchangeTransactionRepository;
import com.prgrms.ijuju.domain.wallet.handler.WebSocketHandler;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class WalletService {
    private final WalletRepository walletRepository;
    private final PointTransactionRepository pointTransactionRepository;
    private final WebSocketHandler webSocketHandler;
    private final ExchangeTransactionRepository exchangeTransactionRepository;

    // 게임 포인트 유효성 검사
    private void validateGamePoints(GamePointRequestDTO request) {
        if (request == null) {
            throw WalletException.INVALID_GAME_POINT.toException();
        }
        
        if (request.getMemberId() == null) {
            throw WalletException.INVALID_MEMBER_ID.toException();
        }
        
        if (request.getPoints() == null || request.getPoints() < 0) {
            throw WalletException.INVALID_AMOUNT.toException();
        }
        
        if (request.getGameType() == null) {
            throw WalletException.INVALID_GAME_TYPE.toException();
        }
    }

    // 환전 처리 유효성 검사
    private void validateExchange(Wallet wallet, Long points) {
        if (points < 100 || points % 100 != 0) {
            throw WalletException.EXCHANGE_UNIT.toException();
        }
        if (wallet.getCurrentPoints() < points) {
            throw WalletException.INSUFFICIENT_POINTS.toException();
        }
    }

    // 주식 투자 유효성 검사
    private void validateStockTransaction(Wallet wallet, StockPointRequestDTO request) {
        if (request.getTransactionType() == TransactionType.USED 
                && wallet.getCurrentPoints() < request.getPoints()) {
            throw WalletException.INSUFFICIENT_POINTS.toException();
        }
    }

    // 현재 포인트 및 코인 조회
    public WalletResponseDTO showCurrentBalance(Long memberId) {
        Wallet wallet = walletRepository.findByMemberId(memberId)
                .orElseThrow(() -> WalletException.WALLET_NOT_FOUND.toException());
        return new WalletResponseDTO(memberId, wallet.getCurrentPoints(), wallet.getCurrentCoins());
    }

    // 환전 처리
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public WalletResponseDTO exchangePointsToCoin(ExchangeRequestDTO request) {
        // 최소 환전 포인트 검증
        if (request.getPointsExchanged() < 100) {
            throw WalletException.EXCHANGE_UNIT.toException();
        }

        Wallet wallet = walletRepository.findByMemberId(request.getMemberId())
                .orElseThrow(() -> WalletException.WALLET_NOT_FOUND.toException());
        
        validateExchange(wallet, request.getPointsExchanged());
        
        // 포인트/코인 업데이트
        wallet.updatePointsAndCoins(request.getPointsExchanged(), TransactionType.EXCHANGED);

        // 1. 포인트 거래 내역 저장
        savePointTransaction(wallet.getMember(), 
                TransactionType.EXCHANGED, 
                request.getPointsExchanged(), 
                PointType.EXCHANGE, 
                "POINTS_TO_COINS");

        // 2. 환전 거래 내역 저장
        ExchangeTransaction exchangeTransaction = ExchangeTransaction.builder()
                .member(wallet.getMember())
                .transactionType(TransactionType.EXCHANGED)
                .pointsExchanged(request.getPointsExchanged())
                .coinsReceived(request.getPointsExchanged() / 100)
                .pointType(PointType.EXCHANGE)
                .build();
        exchangeTransactionRepository.save(exchangeTransaction);
        
        notifyPointUpdate(request.getMemberId(), wallet.getCurrentPoints(), wallet.getCurrentCoins());

        return new WalletResponseDTO(request.getMemberId(), wallet.getCurrentPoints(), wallet.getCurrentCoins());
    }

    // 출석 체크
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public WalletResponseDTO processAttendancePoints(AttendanceRequestDTO request) {
        Wallet wallet = walletRepository.findByMemberId(request.getMemberId())
                .orElseThrow(() -> WalletException.WALLET_NOT_FOUND.toException());
        
        // 오늘 날짜의 시작(00:00:00)과 끝(23:59:59) 설정
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = now.toLocalDate().atTime(23, 59, 59);

        // 오늘의 출석체크 거래내역 확인
        boolean alreadyCheckedIn = pointTransactionRepository.existsByMemberIdAndPointTypeAndCreatedAtBetween(
                request.getMemberId(), 
                PointType.ATTENDANCE, 
                startOfDay, 
                endOfDay
        );

        if (alreadyCheckedIn) {
            throw WalletException.ATTENDANCE_ALREADY_CHECKED.toException();
        }

        Long attendancePoints = 100L;
        wallet.updatePointsAndCoins(attendancePoints, TransactionType.EARNED);

        savePointTransaction(wallet.getMember(), TransactionType.EARNED, 
                attendancePoints, PointType.ATTENDANCE, "CHECKIN");

        notifyPointUpdate(request.getMemberId(), wallet.getCurrentPoints(), wallet.getCurrentCoins());

        return new WalletResponseDTO(request.getMemberId(), wallet.getCurrentPoints(), wallet.getCurrentCoins());
    }

    // 미니 게임
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public WalletResponseDTO processMiniGamePoints(GamePointRequestDTO request) {
        validateGamePoints(request);

        Wallet wallet = walletRepository.findByMemberId(request.getMemberId())
                .orElseThrow(() -> WalletException.WALLET_NOT_FOUND.toException());

        // isWin이 true일 때만 EARNED로 설정, 아니면 MAINTAINED
        TransactionType transactionType = request.isWin() ? TransactionType.EARNED : TransactionType.MAINTAINED;

        // 포인트 업데이트 및 거래 내역 저장
        wallet.updatePointsAndCoins(request.getPoints(), transactionType);
        String description = request.getGameType().toString() + ":" + (request.isWin() ? "승리" : "패배");
        savePointTransaction(wallet.getMember(), transactionType, request.getPoints(), PointType.GAME, description);

        // 실시간 업데이트
        notifyPointUpdate(request.getMemberId(), wallet.getCurrentPoints(), wallet.getCurrentCoins());

        return new WalletResponseDTO(request.getMemberId(), wallet.getCurrentPoints(), wallet.getCurrentCoins());
    }
    
    // 주식 투자
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public WalletResponseDTO simulateStockInvestment(StockPointRequestDTO request) {
        Wallet wallet = walletRepository.findByMemberId(request.getMemberId())
                .orElseThrow(() -> WalletException.WALLET_NOT_FOUND.toException());

        validateStockTransaction(wallet, request);

        wallet.updatePointsAndCoins(request.getPoints(), request.getTransactionType());

        savePointTransaction(wallet.getMember(), request.getTransactionType(), 
                request.getPoints(), PointType.STOCK, request.getStockType().toString());

        notifyPointUpdate(request.getMemberId(), wallet.getCurrentPoints(), wallet.getCurrentCoins());

        return new WalletResponseDTO(request.getMemberId(), wallet.getCurrentPoints(), wallet.getCurrentCoins());
    }

    // 포인트 거래 내역 저장
    private void savePointTransaction(Member member, TransactionType transactionType, 
            Long points, PointType pointType, String subType) {
        PointTransaction transaction = PointTransaction.builder()
                .member(member)
                .transactionType(transactionType)
                .points(points)
                .pointType(pointType)
                .subType(subType)
                .build();
        pointTransactionRepository.save(transaction);
    }

    // 실시간 업데이트
    private void notifyPointUpdate(Long memberId, Long currentPoints, Long currentCoins) {
        WalletResponseDTO response = new WalletResponseDTO(memberId, currentPoints, currentCoins);
        webSocketHandler.sendPointUpdate(memberId, response);
    }
}
