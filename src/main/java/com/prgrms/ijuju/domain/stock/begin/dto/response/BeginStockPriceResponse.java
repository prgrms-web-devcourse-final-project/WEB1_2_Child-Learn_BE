package com.prgrms.ijuju.domain.stock.begin.dto.response;

import com.prgrms.ijuju.domain.stock.begin.entity.BeginStockPrice;

public record BeginStockPriceResponse(String tradeDay, int price) {
    public static BeginStockPriceResponse from(BeginStockPrice beginStock) {
        return new BeginStockPriceResponse(
                beginStock.getTradeDay(),
                beginStock.getPrice()
        );
    }
}
