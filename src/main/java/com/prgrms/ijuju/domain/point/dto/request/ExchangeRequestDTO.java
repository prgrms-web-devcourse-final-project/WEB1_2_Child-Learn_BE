package com.prgrms.ijuju.domain.point.dto.request;

import lombok.Data;

@Data
public class ExchangeRequestDTO { // 포인트 환전 요청 DTO
    private Long memberId;
    private Long pointsExchanged;
} 