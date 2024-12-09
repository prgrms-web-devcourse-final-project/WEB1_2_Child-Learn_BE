package com.prgrms.ijuju.domain.stock.begin.dto.response;

import java.util.List;

public record BeginStockResponse(
        List <BeginStockPriceResponse> stockData,
        List<BeginStockQuizResponse> quiz
) {}
