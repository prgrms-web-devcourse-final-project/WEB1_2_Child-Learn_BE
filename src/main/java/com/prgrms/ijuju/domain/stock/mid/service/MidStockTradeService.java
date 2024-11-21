package com.prgrms.ijuju.domain.stock.mid.service;

import com.prgrms.ijuju.domain.stock.mid.entity.MidStock;
import com.prgrms.ijuju.domain.stock.mid.entity.MidStockPrice;
import com.prgrms.ijuju.domain.stock.mid.entity.MidStockTrade;
import com.prgrms.ijuju.domain.stock.mid.entity.TradeType;
import com.prgrms.ijuju.domain.stock.mid.repository.MidStockPriceRepository;
import com.prgrms.ijuju.domain.stock.mid.repository.MidStockRepository;
import com.prgrms.ijuju.domain.stock.mid.repository.MidStockTradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MidStockTradeService {
    private final MidStockTradeRepository midStockTradeRepository;
    private final MidStockRepository midStockRepository;
    private final MidStockPriceRepository midStockPriceRepository;

    // 멤버의 잔여 포인트가 얼마나 있는지 확인해야 된다.

    // 매수 주문  3가지의 구현이 필요함 (멤버, 포인트 필요)
    public boolean buyStock(Long midStockId, long tradePoint) {
        MidStock midStock = midStockRepository.findById(midStockId)
                .orElseThrow(() -> new IllegalStateException("해당 주식이 존재하지 않습니다."));

        // 거래 가능 여부 확인 추가해야함  tradePoint 와 멤버의 잔여 포인트 그거 계산

        MidStockTrade trade = MidStockTrade.builder()
                .midStock(midStock)
                .tradePoint(tradePoint)
                .tradeType(TradeType.BUY)
                .pricePerStock(getCurrentStockPrice(midStockId))
                .build();

        midStockTradeRepository.save(trade);
        // 포인트로 save하는 거 추가 해야함 (SPENT로)


        // 남은돈 다 넣었을떄 경고로 true false 바꿔야함
        return true;
    }


    // 매도 주문
    public long sellStock(Long midStockId) {



        return calculateSellProfit(midStockId);
    }


    // 거래 가능 여부 확인



    // 현재 주식 가격 조회
    private long getCurrentStockPrice(Long midStockId) {
        return midStockPriceRepository.findTodayPrice(midStockId)
                .map(MidStockPrice::getAvgPrice)    // 나중에 에러 처리 수정
                .orElseThrow(() -> new IllegalStateException("오늘의 주식 가격이 아직 생성되지 않았습니다."));
    }

    // 매도 수익 계산
    private long calculateSellProfit(Long midStockId) {
        return 0;
    }

    // 올인하였을때 경고 판단  이게 애매하네?  남은돈을 다 투자했을때 경고로


}
