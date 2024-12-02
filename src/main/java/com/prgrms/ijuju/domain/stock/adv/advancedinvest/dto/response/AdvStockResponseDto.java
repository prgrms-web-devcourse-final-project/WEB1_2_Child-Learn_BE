package com.prgrms.ijuju.domain.stock.adv.advancedinvest.dto.response;


import com.prgrms.ijuju.domain.stock.adv.advstock.entity.AdvStock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;



@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdvStockResponseDto {
    private String symbol;
    private String name;
    private Double openPrice;
    private Double highPrice;
    private Double lowPrice;
    private Double closePrice;
    private Long timestamp;
    private String dataType;

    public static AdvStockResponseDto fromEntity(AdvStock advStock, int index) {
        return AdvStockResponseDto.builder()
                .symbol(advStock.getSymbol())
                .name(advStock.getName())
                .openPrice(advStock.getOpenPrices().get(index))
                .highPrice(advStock.getHighPrices().get(index))
                .lowPrice(advStock.getLowPrices().get(index))
                .closePrice(advStock.getClosePrices().get(index))
                .timestamp(advStock.getTimestamps().get(index))
                .dataType(advStock.getDataType().name())
                .build();
    }

    public static List<AdvStockResponseDto> fromEntityForReference(AdvStock advStock) {
        List<AdvStockResponseDto> dtoList = new ArrayList<>();
        int dataSize = advStock.getOpenPrices().size(); // 모든 데이터의 크기 가정
        for (int i = 0; i < dataSize; i++) {
            dtoList.add(AdvStockResponseDto.builder()
                    .symbol(advStock.getSymbol())
                    .name(advStock.getName())
                    .openPrice(advStock.getOpenPrices().get(i))
                    .highPrice(advStock.getHighPrices().get(i))
                    .lowPrice(advStock.getLowPrices().get(i))
                    .closePrice(advStock.getClosePrices().get(i))
                    .timestamp(advStock.getTimestamps().get(i))
                    .dataType(advStock.getDataType().name())
                    .build());
        }
        return dtoList;
    }
}

