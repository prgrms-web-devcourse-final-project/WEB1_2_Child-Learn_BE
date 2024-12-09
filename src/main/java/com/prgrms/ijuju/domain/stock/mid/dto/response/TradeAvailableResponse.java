package com.prgrms.ijuju.domain.stock.mid.dto.response;

import lombok.Builder;

@Builder
public record TradeAvailableResponse(
        Boolean isPossibleBuy,
        Boolean isPossibleSell
) {
    public static TradeAvailableResponse of(Boolean isPossibleBuy, Boolean isPossibleSell) {
        return new TradeAvailableResponse(isPossibleBuy, isPossibleSell);
    }
}
