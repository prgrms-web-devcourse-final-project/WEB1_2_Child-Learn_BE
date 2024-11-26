package com.prgrms.ijuju.domain.stock.begin.dto.response;

import com.prgrms.ijuju.domain.stock.begin.entity.BeginStockPrice;

import java.util.List;

public record BeginStockResponse(
        List <BeginStockPriceResponse> stockData,
        List<BeginStockQuizResponse> quiz
) {
    public static BeginStockResponse of(List<BeginStockPrice> stocks) {
        List<BeginStockPriceResponse> stockResponse = stocks.stream()
                .map(BeginStockPriceResponse::from)
                .toList();

        return new BeginStockResponse(stockResponse, null);
    }
}
