package com.prgrms.ijuju.domain.wallet.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeTransactionResponseDTO { 
    
    private Long id;
    private Long memberId;
    private Long pointsExchanged;
    private Long coinsReceived;
    private LocalDateTime createdAt;
} 
