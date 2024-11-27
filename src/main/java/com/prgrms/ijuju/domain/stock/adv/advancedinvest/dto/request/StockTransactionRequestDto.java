package com.prgrms.ijuju.domain.stock.adv.advancedinvest.dto.request;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.stock.adv.stockrecord.constant.TradeType;
import com.prgrms.ijuju.domain.stock.adv.stockrecord.entity.StockRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockTransactionRequestDto {
    private String stockSymbol;    // 주식 종목
    private int quantity;          // 구매 또는 판매 수량
    private Long memberId;         // 사용자 ID
    private BigDecimal points;     // 거래 금액
    private TradeType tradeType;   // 거래 유형 (BUY or SELL)     // 사용자 ID

}