package com.prgrms.ijuju.domain.point.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeDetailsResponseDTO { // 환전 기록 응답 DTO
    private Long exchangeId;
    private Long memberId;
    private Long pointsExchanged;
    private Long coinsReceived;
    private LocalDateTime createdAt;
} 