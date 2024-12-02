package com.prgrms.ijuju.domain.stock.mid.service;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.stock.mid.dto.response.TradeAvailableResponse;
import com.prgrms.ijuju.domain.stock.mid.entity.MidStock;
import com.prgrms.ijuju.domain.stock.mid.entity.MidStockPrice;
import com.prgrms.ijuju.domain.stock.mid.entity.MidStockTrade;
import com.prgrms.ijuju.domain.stock.mid.entity.TradeType;
import com.prgrms.ijuju.domain.stock.mid.exception.*;
import com.prgrms.ijuju.domain.stock.mid.repository.MidStockPriceRepository;
import com.prgrms.ijuju.domain.stock.mid.repository.MidStockRepository;
import com.prgrms.ijuju.domain.stock.mid.repository.MidStockTradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.prgrms.ijuju.domain.wallet.dto.request.PointRequestDTO;
import com.prgrms.ijuju.domain.wallet.dto.request.StockPointRequestDTO;
import com.prgrms.ijuju.domain.wallet.entity.PointType;
import com.prgrms.ijuju.domain.wallet.entity.StockType;
import com.prgrms.ijuju.domain.wallet.entity.TransactionType;
import com.prgrms.ijuju.domain.wallet.service.WalletService;
import com.prgrms.ijuju.global.exception.CustomException;
import com.prgrms.ijuju.domain.wallet.entity.Wallet;
import com.prgrms.ijuju.domain.wallet.repository.WalletRepository;
import com.prgrms.ijuju.domain.wallet.exception.WalletException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MidStockTradeService {
    private final MidStockTradeRepository midStockTradeRepository;
    private final MidStockRepository midStockRepository;
    private final MidStockPriceRepository midStockPriceRepository;
    private final MemberRepository memberRepository;
    private final WalletService WalletService;
    private final WalletRepository walletRepository;

    // 매수 주문  3가지의 구현이 필요함 (멤버, 포인트 필요)
    public boolean buyStock(Long memberId, Long midStockId, long tradePoint) {
        MidStock midStock = midStockRepository.findById(midStockId)
                .orElseThrow(MidStockNotFoundException::new);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(MidMemberNotFoundException::new);
        // 오늘 매수 했는지 확인
        Optional<MidStockTrade> todayBuyMidStock = midStockTradeRepository.findTodayBuyMidStock(memberId, midStockId);
        if (todayBuyMidStock.isPresent()) {
            throw new MidAlreadyBoughtException();
        }
        // 포인트가 충분한지 거래 가능 여부 확인 추가해야함  tradePoint 와 멤버의 잔여 포인트 그거 계산
        if (!isTradeAvailable(member, tradePoint)) {
            throw new MidPointsNotEnoughException();
        }

        boolean isWarning = isAllInWarning(member, tradePoint);

        // 포인트 차감
        StockPointRequestDTO stockPointRequestDTO = StockPointRequestDTO.builder()
                .memberId(memberId)
                .points(tradePoint)
                .pointType(PointType.STOCK)
                .transactionType(TransactionType.USED)
                .stockType(StockType.MID)
                .build();
        WalletService.simulateStockInvestment(stockPointRequestDTO);
        // 거래 내역 저장
        MidStockTrade trade = MidStockTrade.builder()
                .midStock(midStock)
                .tradePoint(tradePoint)
                .tradeType(TradeType.BUY)
                .pricePerStock(getCurrentStockPrice(midStockId))
                .member(member)
                .build();
        midStockTradeRepository.save(trade);

        return isWarning;
    }

    // 매도 주문
    public long sellStock(Long memberId, Long midStockId) {
        long totalPoints = 0; // 매도시 포인트
        long investedPoints = 0; //투자한 포인트
        // 보유중인 주식 조회
        List<MidStockTrade> buyMidStock = midStockTradeRepository.findBuyMidStock(memberId, midStockId);
        if (buyMidStock.isEmpty()) {
            throw new MidStockNotFoundException();
        }

        // 오늘 매도 했는지 확인
        Optional<MidStockTrade> todaySellMidStock = midStockTradeRepository.findTodaySellMidStock(memberId, midStockId);
        if (todaySellMidStock.isPresent()) {
            throw new MidAlreadySoldException();
        }

        // 매수 -> 매도
        for (MidStockTrade midStockTrade : buyMidStock) {
            midStockTrade.changeTradeType(TradeType.SELL);
        }

        // 수익 계산
        long todayAvgPrice = midStockPriceRepository.findTodayAvgPrice(midStockId);
        for (MidStockTrade midStockTrade : buyMidStock) {
            double rate = (double) todayAvgPrice / midStockTrade.getPricePerStock();
            totalPoints += Math.round(midStockTrade.getTradePoint() * rate);
            investedPoints += midStockTrade.getTradePoint();
        }

        // 포인트 처리 로직 추가해야함
        StockPointRequestDTO stockPointRequestDTO = StockPointRequestDTO.builder()
                .memberId(memberId)
                .points(totalPoints)
                .pointType(PointType.STOCK)
                .transactionType(TransactionType.EARNED)
                .stockType(StockType.MID)
                .build();
        WalletService.simulateStockInvestment(stockPointRequestDTO);

        return totalPoints - investedPoints;
    }

    // 하루한번 거래가능 체크
    public TradeAvailableResponse isTradeAvailable(Long memberId, Long midStockId) {
        boolean isPossibleBuy;
        boolean isPossibleSell;

        Member member = memberRepository.findById(memberId)
                .orElseThrow(MidMemberNotFoundException::new);

        MidStock midStock = midStockRepository.findById(midStockId)
                .orElseThrow(MidStockNotFoundException::new);

        Optional<MidStockTrade> todayBuyMidStock = midStockTradeRepository.findTodayBuyMidStock(memberId, midStockId);
        Optional<MidStockTrade> todaySellMidStock = midStockTradeRepository.findTodaySellMidStock(memberId, midStockId);

        isPossibleBuy = todayBuyMidStock.isEmpty();
        isPossibleSell = todaySellMidStock.isEmpty();

        return TradeAvailableResponse.builder()
                .isPossibleBuy(isPossibleBuy)
                .isPossibleSell(isPossibleSell)
                .build();
    }

    // 거래 가능 여부 확인
    private boolean isTradeAvailable(Member member, long tradePoint) {
        Wallet wallet = walletRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new CustomException(WalletException.WALLET_NOT_FOUND.getMessage()));
        return wallet.getCurrentPoints() >= tradePoint;
    }

    // 현재 주식 가격 조회
    private long getCurrentStockPrice(Long midStockId) {
        return midStockPriceRepository.findTodayPrice(midStockId)
                .map(MidStockPrice::getAvgPrice)
                .orElseThrow(MidPriceNotFoundException::new);
    }

    // 올인하였을때 경고 판단 - 남은돈을 다 투자했을때 경고로
    private boolean isAllInWarning(Member member, long tradePoint) {
        Wallet wallet = walletRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new CustomException(WalletException.WALLET_NOT_FOUND.getMessage()));
        return wallet.getCurrentPoints() == tradePoint;
    }
}