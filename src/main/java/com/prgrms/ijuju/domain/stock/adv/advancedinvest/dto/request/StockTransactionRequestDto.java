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
    private Long memberId;
    private String stockSymbol;
    private int quantity;
    private BigDecimal points;
    private TradeType tradeType;

    public StockRecord toEntity(Member member) {
        return StockRecord.builder()
                .member(member)
                .symbol(stockSymbol)
                .tradeType(tradeType)
                .pricePerUnit(points)
                .quantity(quantity)
                .tradeDate(java.time.LocalDateTime.now())
                .build();
    }
}