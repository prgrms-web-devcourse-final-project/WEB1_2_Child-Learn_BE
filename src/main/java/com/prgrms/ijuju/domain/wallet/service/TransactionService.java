package com.prgrms.ijuju.domain.wallet.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ijuju.domain.wallet.dto.response.ExchangeTransactionResponseDTO;
import com.prgrms.ijuju.domain.wallet.dto.response.PointTransactionResponseDTO;
import com.prgrms.ijuju.domain.wallet.dto.response.WalletResponseDTO;
import com.prgrms.ijuju.domain.wallet.entity.ExchangeTransaction;
import com.prgrms.ijuju.domain.wallet.entity.PointTransaction;
import com.prgrms.ijuju.domain.wallet.repository.PointTransactionRepository;
import com.prgrms.ijuju.domain.wallet.repository.ExchangeTransactionRepository;
import com.prgrms.ijuju.domain.wallet.entity.PointType;
import com.prgrms.ijuju.domain.wallet.entity.TransactionType;
import com.prgrms.ijuju.domain.wallet.exception.WalletException;
import com.prgrms.ijuju.domain.wallet.repository.WalletRepository;
import com.prgrms.ijuju.domain.wallet.entity.Wallet;
import com.prgrms.ijuju.global.exception.CustomException;
import com.prgrms.ijuju.domain.wallet.handler.WebSocketHandler;
import com.prgrms.ijuju.domain.member.entity.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class TransactionService {
    private final PointTransactionRepository pointTransactionRepository;
    private final ExchangeTransactionRepository exchangeTransactionRepository;
    private final WalletRepository walletRepository;
    private final WebSocketHandler webSocketHandler;

    // 포인트 거래 내역 저장
    @Transactional
    public void saveTransaction(Member member, TransactionType transactionType, 
                              Long points, PointType pointType, String subType) {
        if (member == null || points == null) {
            throw new IllegalArgumentException("필수 파라미터가 누락되었습니다.");
        }
        
        PointTransaction transaction = PointTransaction.builder()
            .member(member)
            .transactionType(transactionType)
            .points(points)
            .pointType(pointType)
            .subType(subType)
            .description(generateDescription(transactionType, points, pointType))
            .build();
            
        pointTransactionRepository.save(transaction);
        
        Wallet wallet = walletRepository.findByMemberId(member.getId())
            .orElseThrow(() -> new CustomException(WalletException.WALLET_NOT_FOUND));
            
        notifyPointUpdate(member.getId(), wallet.getCurrentPoints(), wallet.getCurrentCoins());
    }

    private String generateDescription(TransactionType transactionType, 
                                     Long points, PointType pointType) {
        return String.format("%s: %d %s", 
            transactionType.toString(), 
            points, 
            pointType.toString());
    }

    // 포인트 거래 내역 조회
    public List<PointTransactionResponseDTO> showTransactionHistory(Long memberId) {
        return pointTransactionRepository.findByMemberIdOrderByCreatedAtDesc(memberId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    // 거래 유형별(transactionType) 포인트 내역 조회
    public List<PointTransactionResponseDTO> showTransactionsByType(
            Long memberId, TransactionType transactionType) {
        return pointTransactionRepository
            .findByMemberIdAndTransactionTypeOrderByCreatedAtDesc(memberId, transactionType)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }


    // 포인트 유형별(pointType) 내역 조회
    public List<PointTransactionResponseDTO> showTransactionsByPointType(
            Long memberId, PointType pointType) {
        return pointTransactionRepository
            .findByMemberIdAndPointTypeOrderByCreatedAtDesc(memberId, pointType)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    // 주간별 내역 조회
    public List<PointTransactionResponseDTO> showWeeklyTransactions(
            Long memberId, LocalDateTime startDate, LocalDateTime endDate) {
        return pointTransactionRepository
            .findByMemberIdAndCreatedAtBetweenOrderByCreatedAtDesc(
                memberId, startDate, endDate)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    // 환전내역 조회
    public List<ExchangeTransactionResponseDTO> showExchangeHistory(Long memberId) {
        return exchangeTransactionRepository
            .findByMemberIdOrderByCreatedAtDesc(memberId)
            .stream()
            .map(this::convertToExchangeDTO)
            .collect(Collectors.toList());
    }

    // 환전내역 DTO 변환
    private ExchangeTransactionResponseDTO convertToExchangeDTO(ExchangeTransaction transaction) {
        return new ExchangeTransactionResponseDTO(
            transaction.getId(),
            transaction.getMember().getId(),
            transaction.getPointsExchanged(),
            transaction.getCoinsReceived(),
            transaction.getCreatedAt()
        );
    }

    // 포인트 거래 내역 DTO 변환
    private PointTransactionResponseDTO convertToDTO(PointTransaction transaction) {
        return new PointTransactionResponseDTO(
            transaction.getId(),
            transaction.getMember().getId(),
            transaction.getTransactionType().toString(),
            transaction.getPoints(),
            transaction.getPointType().toString(),
            transaction.getSubType(),
            transaction.getDescription(),
            transaction.getCreatedAt()
        );
    }
    

    // 실시간 업데이트
    private void notifyPointUpdate(Long memberId, Long currentPoints, Long currentCoins) {
        try {
            webSocketHandler.sendPointUpdate(memberId, new WalletResponseDTO(memberId, currentPoints, currentCoins));
        } catch (Exception e) {
            log.error("실시간 포인트 업데이트 실패: memberId={}, error={}", 
                     memberId, e.getMessage());
            throw new CustomException(WalletException.REALTIME_SYNC_FAILED);
        }
    }
}
