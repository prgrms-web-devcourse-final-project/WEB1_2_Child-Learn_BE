package com.prgrms.ijuju.domain.stock.mid.dto.response;

import com.prgrms.ijuju.domain.stock.mid.entity.MidStock;
import lombok.Builder;

@Builder
public record MidStockResponse(
        Long midStockId,
        String midName
) {
    public static MidStockResponse of (MidStock midStock) {
        return new MidStockResponse(
                midStock.getId(),
                midStock.getStockName()
        );
    }
}
