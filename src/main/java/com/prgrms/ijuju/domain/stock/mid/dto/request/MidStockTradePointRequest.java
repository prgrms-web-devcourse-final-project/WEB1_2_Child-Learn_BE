package com.prgrms.ijuju.domain.stock.mid.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class MidStockTradePointRequest {
    @Positive
    private long tradePoint;
}
