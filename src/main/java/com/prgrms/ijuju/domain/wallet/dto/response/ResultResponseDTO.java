package com.prgrms.ijuju.domain.wallet.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResultResponseDTO {
    
    private String message;
    private Long currentPoints;
    private Long currentCoins;
} 