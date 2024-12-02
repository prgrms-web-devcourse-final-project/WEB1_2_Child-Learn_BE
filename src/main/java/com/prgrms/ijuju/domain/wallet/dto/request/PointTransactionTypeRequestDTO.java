package com.prgrms.ijuju.domain.wallet.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PointTransactionTypeRequestDTO {
    
    private Long memberId;
    private String transactionType;

    public PointTransactionTypeRequestDTO(Long memberId) {
        this.memberId = memberId;
    }
} 
