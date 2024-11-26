package com.prgrms.ijuju.domain.stock.adv.advancedinvest.service;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.point.dto.request.PointRequestDTO;
import com.prgrms.ijuju.domain.point.entity.StockStatus;
import com.prgrms.ijuju.domain.point.entity.StockType;
import com.prgrms.ijuju.domain.point.service.PointService;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.dto.request.AdvancedInvestRequestDto;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.dto.request.StockTransactionRequestDto;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.dto.response.AdvancedInvestResponseDto;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.dto.response.StockResponseDto;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.entity.AdvancedInvest;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.repository.AdvancedInvestRepository;
import com.prgrms.ijuju.domain.stock.adv.advstock.entity.AdvStock;
import com.prgrms.ijuju.domain.stock.adv.advstock.repository.AdvStockRepository;
import com.prgrms.ijuju.domain.stock.adv.stockrecord.constant.TradeType;
import com.prgrms.ijuju.domain.stock.adv.stockrecord.dto.request.StockRecordRequestDto;
import com.prgrms.ijuju.domain.stock.adv.stockrecord.service.StockRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdvancedInvestServiceImpl implements AdvancedInvestService {

    private final AdvancedInvestRepository advancedInvestRepository;
    private final AdvStockRepository advStockRepository;
    private final MemberRepository memberRepository;
    private final StockRecordService stockRecordService;
    private final PointService pointService;

    @Transactional
    @Override
    public AdvancedInvestResponseDto startGame(AdvancedInvestRequestDto request) {

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다."));

        AdvancedInvest advancedInvest = advancedInvestRepository.save(
                AdvancedInvest.builder()
                        .member(member)
                        .startTime(System.currentTimeMillis())
                        .paused(false)
                        .playedToday(true)
                        .build()
        );
        return AdvancedInvestResponseDto.from(advancedInvest);
    }

    @Transactional
    @Override
    public void pauseGame(Long advId) {
        AdvancedInvest advancedInvest = getAdvancedInvestById(advId);
        advancedInvest.setPaused(true);
    }

    @Transactional
    @Override
    public void resumeGame(Long advId) {
        AdvancedInvest advancedInvest = getAdvancedInvestById(advId);
        advancedInvest.setPaused(false);
    }

    @Transactional
    @Override
    public void endGame(Long advId) {
        AdvancedInvest advancedInvest = getAdvancedInvestById(advId);
        advancedInvest.setPlayedToday(false);
        advancedInvestRepository.save(advancedInvest);
    }

    @Transactional(readOnly = true)
    @Override
    public List<StockResponseDto> getReferenceData(Long advId) {
        return advStockRepository.findAllByDataType("REFERENCE")
                .stream()
                .map(StockResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public StockResponseDto getLiveData(Long advId, String symbol, int hour) {
        List<AdvStock> stocks = advStockRepository.findBySymbolAndAdvIdOrderByTimestampAsc(symbol, advId);

        if (hour <= 0 || hour > stocks.size()) {
            throw new IllegalArgumentException("유효하지 않은 hour 값입니다.");
        }

        AdvStock stock = stocks.get(hour - 1);

        return StockResponseDto.fromEntity(stock);
    }

    @Transactional
    @Override
    public void buyStock(Long advId, StockTransactionRequestDto request) {
        AdvancedInvest advancedInvest = getAdvancedInvestById(advId);

        // 주식 구매에 필요한 포인트 계산
        BigDecimal pointsRequired = request.getPoints().multiply(BigDecimal.valueOf(request.getQuantity()));
        // 포인트 차감 및 거래 기록
        PointRequestDTO pointRequest = PointRequestDTO.builder()
                .memberId(request.getMemberId())
                .pointAmount(pointsRequired.longValue())
                .build();

        pointService.simulateStockInvestment(pointRequest, StockType.valueOf(request.getStockSymbol()), StockStatus.BUY);

        StockRecordRequestDto recordRequest = StockRecordRequestDto.builder()
                .memberId(request.getMemberId())
                .stockSymbol(request.getStockSymbol())
                .tradeType(TradeType.BUY)
                .quantity(request.getQuantity())
                .price(pointsRequired)
                .advId(advId)
                .build();

        // 거래 내역 저장
        stockRecordService.saveRecord(recordRequest, advancedInvest.getMember());
    }

    @Transactional
    @Override
    public void sellStock(Long advId, StockTransactionRequestDto request) {
        AdvancedInvest advancedInvest = getAdvancedInvestById(advId);

        // 보유 주식 수량 확인
        int ownedQuantity = stockRecordService.calculateOwnedStock(advId, request.getStockSymbol());
        if (ownedQuantity < request.getQuantity()) {
            throw new IllegalArgumentException("보유 수량보다 많은 주식을 판매할 수 없습니다.");
        }

        // 판매로 얻는 포인트 계산
        BigDecimal pointsEarned = request.getPoints().multiply(BigDecimal.valueOf(request.getQuantity()));

        // 포인트 환급 및 거래 기록
        PointRequestDTO pointRequest = PointRequestDTO.builder()
                .memberId(request.getMemberId())
                .pointAmount(pointsEarned.longValue())
                .build();

        pointService.simulateStockInvestment(pointRequest, StockType.valueOf(request.getStockSymbol()), StockStatus.SELL);

        StockRecordRequestDto recordRequest = StockRecordRequestDto.builder()
                .memberId(request.getMemberId())
                .stockSymbol(request.getStockSymbol())
                .tradeType(TradeType.SELL)
                .quantity(request.getQuantity())
                .price(pointsEarned)
                .advId(advId)
                .build();

        // 거래 내역 저장
        stockRecordService.saveRecord(recordRequest, advancedInvest.getMember());
    }

    private AdvancedInvest getAdvancedInvestById(Long advId) {
        return advancedInvestRepository.findById(advId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게임입니다."));
    }
}}