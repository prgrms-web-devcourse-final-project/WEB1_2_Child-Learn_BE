package com.prgrms.ijuju.domain.stock.adv.advancedinvest.dto.response;


import com.prgrms.ijuju.domain.stock.adv.advstock.entity.AdvStock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockResponseDto {
    private String symbol;
    private String name;
    private List<Double> openPrices;
    private List<Double> highPrices;
    private List<Double> lowPrices;
    private List<Double> closePrices;
    private List<Long> timestamps;

    public static StockResponseDto fromEntity(AdvStock advStock) {
        return StockResponseDto.builder()
                .symbol(advStock.getSymbol())
                .name(advStock.getName())
                .openPrices(advStock.getOpenPrices())
                .highPrices(advStock.getHighPrices())
                .lowPrices(advStock.getLowPrices())
                .closePrices(advStock.getClosePrices())
                .timestamps(advStock.getTimestamps())
                .build();
    }
}