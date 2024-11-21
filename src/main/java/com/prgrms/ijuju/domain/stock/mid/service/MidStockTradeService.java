package com.prgrms.ijuju.domain.stock.mid.service;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.stock.mid.entity.MidStock;
import com.prgrms.ijuju.domain.stock.mid.entity.MidStockPrice;
import com.prgrms.ijuju.domain.stock.mid.entity.MidStockTrade;
import com.prgrms.ijuju.domain.stock.mid.entity.TradeType;
import com.prgrms.ijuju.domain.stock.mid.exception.MemberNotFoundException;
import com.prgrms.ijuju.domain.stock.mid.exception.PointsNotEnoughException;
import com.prgrms.ijuju.domain.stock.mid.exception.PriceNotFoundException;
import com.prgrms.ijuju.domain.stock.mid.exception.StockNotFoundException;
import com.prgrms.ijuju.domain.stock.mid.repository.MidStockPriceRepository;
import com.prgrms.ijuju.domain.stock.mid.repository.MidStockRepository;
import com.prgrms.ijuju.domain.stock.mid.repository.MidStockTradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MidStockTradeService {
    private final MidStockTradeRepository midStockTradeRepository;
    private final MidStockRepository midStockRepository;
    private final MidStockPriceRepository midStockPriceRepository;
    private final MemberRepository memberRepository;

    // 매수 주문  3가지의 구현이 필요함 (멤버, 포인트 필요)
    public boolean buyStock(Long memberId, Long midStockId, long tradePoint) {
        MidStock midStock = midStockRepository.findById(midStockId)
                .orElseThrow(StockNotFoundException::new);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        // 거래 가능 여부 확인 추가해야함  tradePoint 와 멤버의 잔여 포인트 그거 계산
        if (!isTradeAvailable(member, tradePoint)) {
            throw new PointsNotEnoughException();
        }

        // 포인트로 save하는 거 추가 해야함 (SPENT로) ,
        // 포인트 차감
        


        // 거래 내역 저장
        MidStockTrade trade = MidStockTrade.builder()
                .midStock(midStock)
                .tradePoint(tradePoint)
                .tradeType(TradeType.BUY)
                .pricePerStock(getCurrentStockPrice(midStockId))
                .member(member)
                .build();

        midStockTradeRepository.save(trade);

        return isAllInWarning(member, tradePoint);
    }


    // 매도 주문
    public long sellStock(Long memberId, Long midStockId) {
        // 보유중인 주식 조회
        List<MidStockTrade> buyMidStock = midStockTradeRepository.findBuyMidStock(memberId, midStockId);
        if (buyMidStock.isEmpty()) {
            throw new StockNotFoundException();
        }

        // 매수 -> 매도
        for (MidStockTrade midStockTrade : buyMidStock) {
            midStockTrade.changeTradeType(TradeType.SELL);
        }

        // 포인트 처리 로직 추가해야함

        return calculateSellProfit(midStockId);
    }


    // 거래 가능 여부 확인
    private boolean isTradeAvailable(Member member, long tradePoint) {
        Long points = member.getPoints();
        return points >= tradePoint;
    }


    // 현재 주식 가격 조회
    private long getCurrentStockPrice(Long midStockId) {
        return midStockPriceRepository.findTodayPrice(midStockId)
                .map(MidStockPrice::getAvgPrice)
                .orElseThrow(PriceNotFoundException::new);
    }

    // 매도 수익 계산
    private long calculateSellProfit(Long midStockId) {
        return 0;
    }


    // 올인하였을때 경고 판단  이게 애매하네? -> 남은돈을 다 투자했을때 경고로
    private boolean isAllInWarning(Member member, long tradePoint) {
        Long points = member.getPoints();
        return points == tradePoint;
    }

}
