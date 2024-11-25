package com.prgrms.ijuju.domain.stock.begin.dto.response;

import com.prgrms.ijuju.domain.stock.begin.entity.BeginStockGraph;

public record BeginStockGraphResponse(String tradeDay, int price) {
    public static BeginStockGraphResponse from(BeginStockGraph beginStock) {
        return new BeginStockGraphResponse(
                beginStock.getTradeDay(),
                beginStock.getPrice()
        );
    }
}
