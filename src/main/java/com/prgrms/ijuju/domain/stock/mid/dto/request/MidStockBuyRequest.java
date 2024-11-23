package com.prgrms.ijuju.domain.stock.mid.dto.request;

import jakarta.validation.constraints.Positive;

public record MidStockBuyRequest(
        @Positive(message = "종목 ID는 양수여야 합니다")
        int midStockId,

        @Positive(message = "거래 포인트는 양수여야 합니다")
        long tradePoint
) {}
