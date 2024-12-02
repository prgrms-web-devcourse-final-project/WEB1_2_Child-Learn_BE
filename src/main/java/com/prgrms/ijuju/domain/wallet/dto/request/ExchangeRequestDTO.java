package com.prgrms.ijuju.domain.wallet.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRequestDTO {
    
    private Long memberId;
    private Long pointsExchanged;
} 
