package com.prgrms.ijuju.domain.stock.mid.dto.response;

import com.prgrms.ijuju.domain.stock.mid.entity.MidStock;
import com.prgrms.ijuju.domain.stock.mid.entity.MidStockTrade;
import lombok.Builder;

import java.util.List;

@Builder
public record MidStockWithTradesResponse(
        Long midStockId,
        String midName,
        List<MidStockTradeInfo> trades
) {
    public static MidStockWithTradesResponse of(List<MidStockTrade> trades) {
        if (trades.isEmpty()) {
            return null;
        }

        MidStock stock = trades.get(0).getMidStock();
        return new MidStockWithTradesResponse(
                stock.getId(),
                stock.getStockName(),
                trades.stream()
                        .map(MidStockTradeInfo::of)
                        .toList()
        );
    }
}
