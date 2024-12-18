package com.prgrms.ijuju.domain.stock.mid.dto.response;

import com.prgrms.ijuju.domain.stock.mid.entity.MidStockTrade;
import com.prgrms.ijuju.domain.stock.mid.entity.TradeType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MidStockTradeInfo(
        Long tradePoint,
        Long pricePerStock,
        LocalDateTime tradeDate,
        TradeType tradeType
) {
    public static MidStockTradeInfo of (MidStockTrade midStockTrade) {
        return new MidStockTradeInfo(
                midStockTrade.getTradePoint(),
                midStockTrade.getPricePerStock(),
                midStockTrade.getCreatedAt(),
                midStockTrade.getTradeType()
        );
    }
}
