package com.prgrms.ijuju.domain.stock.adv.stockrecord.dto.request;


import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.stock.adv.stockrecord.constant.TradeType;
import com.prgrms.ijuju.domain.stock.adv.stockrecord.entity.StockRecord;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockRecordRequestDto {
    private String stockSymbol;
    private TradeType tradeType;
    private BigDecimal price;
    private int quantity;
    private Long advId;
    private Long memberId;

    public StockRecord toEntity(Member member) {
        return StockRecord.builder()
                .member(member)
                .symbol(stockSymbol)
                .tradeType(tradeType)
                .pricePerUnit(price)
                .quantity(quantity)
                .tradeDate(LocalDateTime.now())
                .build();
    }
}