package com.prgrms.ijuju.domain.stock.adv.advancedinvest.service;

import com.prgrms.ijuju.domain.point.service.PointService;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.constant.GameStage;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.dto.request.AdvancedInvestRequestDto;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.dto.response.AdvancedInvestResponseDto;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.entity.AdvancedInvest;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.repository.AdvancedInvestRepository;
import com.prgrms.ijuju.domain.stock.adv.advstock.service.AdvStockService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdvancedInvestService {

    private final AdvancedInvestRepository advancedInvestRepository;
    private final AdvStockService advStockService;
    private final PointService pointService;

    // 게임 시작
    @Transactional
    public AdvancedInvestResponseDto startGame(AdvancedInvestRequestDto request) {
        AdvancedInvest advancedInvest = advancedInvestRepository.save(
                AdvancedInvest.builder()
                        .memberId(request.getMemberId())
                        .startTime(LocalDateTime.now())
                        .paused(false)
                        .playedToday(true)
                        .currentStage(GameStage.PRE_MARKET) // 장전 거래 시간
                        .remainingTime(Duration.ofMinutes(8).plusSeconds(30))
                        .build()
        );
        return AdvancedInvestResponseDto.from(advancedInvest);
    }

    // 게임 일시정지
    @Transactional
    public void pauseGame(Long advId) {
        AdvancedInvest advancedInvest = advancedInvestRepository.findById(advId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게임입니다."));
        advancedInvest.setPaused(true);
    }

    // 게임 재개
    @Transactional
    public void resumeGame(Long advId) {
        AdvancedInvest advancedInvest = advancedInvestRepository.findById(advId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게임입니다."));
        advancedInvest.setPaused(false);
    }

    // Reference 데이터 가져오기
    @Transactional(readOnly = true)
    public List<StockResponseDto> getReferenceData(Long advId) {
        return stockService.getReferenceData().stream()
                .map(StockResponseDto::from)
                .collect(Collectors.toList());
    }

    // Live 데이터 가져오기
    @Transactional(readOnly = true)
    public StockResponseDto getLiveData(Long advId, String symbol, int hour) {
        return StockResponseDto.from(stockService.getLiveData(symbol, hour));
    }




    // 주식 구매
    @Transactional
    public void buyStock(Long advId, StockTransactionRequestDto request) {
        AdvancedInvest advancedInvest = advancedInvestRepository.findById(advId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게임입니다."));

        String stockSymbol = request.getStockSymbol();
        double lastClosePrice;

        // Reference 데이터 기준 구매
        if (advancedInvest.getCurrentStage() == GameStage.PRE_MARKET) {
            lastClosePrice = stockService.getReferenceData().stream()
                    .filter(stock -> stock.getSymbol().equals(stockSymbol))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("해당 종목의 Reference 데이터를 찾을 수 없습니다."))
                    .getClosePrices().get(0);
        } else {
            // Live 데이터 기준 구매
            lastClosePrice = stockService.getLiveData(stockSymbol, request.getHour()).getClosePrices().get(0);
        }

        long totalCost = (long) (lastClosePrice * request.getQuantity());
        pointService.investPoints(request.getMemberId(), totalCost, StockType.valueOf(stockSymbol), 0L);

        // 구매 내역 저장
        StockRecord stockRecord = StockRecord.builder()
                .memberId(request.getMemberId())
                .symbol(stockSymbol)
                .quantity(request.getQuantity())
                .pricePerUnit(lastClosePrice)
                .transactionType(TransactionType.BUY)
                .build();
        stockRecordRepository.save(stockRecord);
    }

    // 주식 판매
    @Transactional
    public void sellStock(Long advId, StockTransactionRequestDto request) {
        AdvancedInvest advancedInvest = advancedInvestRepository.findById(advId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게임입니다."));

        String stockSymbol = request.getStockSymbol();
        double lastClosePrice = stockService.getLiveData(stockSymbol, request.getHour()).getClosePrices().get(0);

        StockRecord stockRecord = stockRecordRepository.findByMemberIdAndSymbol(request.getMemberId(), stockSymbol)
                .orElseThrow(() -> new IllegalArgumentException("보유하지 않은 주식입니다."));

        if (stockRecord.getQuantity() < request.getQuantity()) {
            throw new IllegalArgumentException("보유 수량보다 많은 양을 판매할 수 없습니다.");
        }

        // 포인트 환급
        long totalEarnings = (long) (lastClosePrice * request.getQuantity());
        pointService.investPoints(request.getMemberId(), 0L, StockType.valueOf(stockSymbol), totalEarnings);

        // 보유 주식 수량 감소 및 기록
        stockRecord.decreaseQuantity(request.getQuantity());
        if (stockRecord.getQuantity() == 0) {
            stockRecordRepository.delete(stockRecord);
        } else {
            stockRecordRepository.save(stockRecord);
        }
    }