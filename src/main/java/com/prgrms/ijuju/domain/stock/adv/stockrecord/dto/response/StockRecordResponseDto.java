package com.prgrms.ijuju.domain.stock.adv.stockrecord.dto.response;

import com.prgrms.ijuju.domain.stock.adv.stockrecord.constant.TradeType;
import com.prgrms.ijuju.domain.stock.adv.stockrecord.entity.StockRecord;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockRecordResponseDto {
    private Long tradeId;
    private String stockSymbol;
    private TradeType tradeType;
    private BigDecimal price;
    private double quantity;
    private LocalDateTime tradeDate;

    public static StockRecordResponseDto fromEntity(StockRecord stockRecord) {
        return StockRecordResponseDto.builder()
                .tradeId(stockRecord.getId())
                .stockSymbol(stockRecord.getSymbol())
                .tradeType(stockRecord.getTradeType())
                .price(stockRecord.getPricePerUnit())
                .quantity(stockRecord.getQuantity())
                .tradeDate(stockRecord.getTradeDate())
                .build();
    }
}