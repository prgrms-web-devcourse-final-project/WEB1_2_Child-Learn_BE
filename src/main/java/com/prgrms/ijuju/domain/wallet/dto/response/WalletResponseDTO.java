package com.prgrms.ijuju.domain.wallet.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WalletResponseDTO {
    
    private Long memberId;
    private Long currentPoints;
    private Long currentCoins;
} 
