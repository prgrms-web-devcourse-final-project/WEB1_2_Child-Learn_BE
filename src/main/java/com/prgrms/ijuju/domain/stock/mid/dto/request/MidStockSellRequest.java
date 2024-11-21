package com.prgrms.ijuju.domain.stock.mid.dto.request;

import jakarta.validation.constraints.Positive;

public record MidStockSellRequest(
        @Positive(message = "종목 ID는 양수여야 합니다")
        int midStockId
) {}
