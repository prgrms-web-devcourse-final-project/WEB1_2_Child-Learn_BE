package com.prgrms.ijuju.domain.wallet.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PointTypeTransactionRequestDTO {
    
    private Long memberId;
    private String pointType;

    public PointTypeTransactionRequestDTO(Long memberId) {
        this.memberId = memberId;
    }
} 
