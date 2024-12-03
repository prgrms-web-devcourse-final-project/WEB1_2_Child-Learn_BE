package com.prgrms.ijuju.domain.stock.mid.dto.response;

import com.prgrms.ijuju.domain.stock.mid.entity.MidStockPrice;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public record MidStockPriceResponse(
        long highPrice,
        long lowPrice,
        long avgPrice,
        LocalDateTime priceDate
) {
    public static MidStockPriceResponse of(MidStockPrice midStockPrice) {
        return new MidStockPriceResponse(
                midStockPrice.getHighPrice(),
                midStockPrice.getLowPrice(),
                midStockPrice.getAvgPrice(),
                midStockPrice.getPriceDate()
        );
    }
}
