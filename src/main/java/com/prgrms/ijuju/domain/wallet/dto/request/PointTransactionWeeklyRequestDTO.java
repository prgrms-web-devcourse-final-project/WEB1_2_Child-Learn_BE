package com.prgrms.ijuju.domain.wallet.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PointTransactionWeeklyRequestDTO {
    
    private Long memberId;
    private String startDate;
    private String endDate;

    public PointTransactionWeeklyRequestDTO(Long memberId) {
        this.memberId = memberId;
    }
} 
