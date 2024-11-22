package com.prgrms.ijuju.domain.point.dto.request;

import lombok.Data;

@Data
public class ExchangeRequestDTO { // 포인트 환전 요청 DTO
    private Long memberId; // 회원 ID
    private Long pointsToExchange; // 환전할 포인트 수
} 