package com.prgrms.ijuju.domain.stock.begin.dto.response;

import com.prgrms.ijuju.domain.stock.begin.entity.BeginStockGraph;

import java.util.List;

public record BeginStockResponse(
        List <BeginStockGraphResponse> stockData,
        List<BeginStockQuizResponse> quiz
) {
    public static BeginStockResponse of(List<BeginStockGraph> stocks) {
        List<BeginStockGraphResponse> stockResponse = stocks.stream()
                .map(BeginStockGraphResponse::from)
                .toList();

        return new BeginStockResponse(stockResponse, null);
    }
}
