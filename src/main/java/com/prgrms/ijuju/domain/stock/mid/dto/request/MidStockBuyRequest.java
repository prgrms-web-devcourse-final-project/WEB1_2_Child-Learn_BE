package com.prgrms.ijuju.domain.stock.mid.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class MidStockBuyRequest {
    private int midStockId;
    private long tradePoint;
}
