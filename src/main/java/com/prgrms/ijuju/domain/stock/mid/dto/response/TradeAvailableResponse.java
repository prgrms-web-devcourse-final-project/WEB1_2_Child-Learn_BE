package com.prgrms.ijuju.domain.stock.mid.dto.response;

import lombok.Builder;

@Builder
public record TradeAvailableResponse(
        boolean isPossibleBuy,
        boolean isPossibleSell
) {
    public static TradeAvailableResponse of(boolean isPossibleBuy, boolean isPossibleSell) {
        return new TradeAvailableResponse(isPossibleBuy, isPossibleSell);
    }
}
