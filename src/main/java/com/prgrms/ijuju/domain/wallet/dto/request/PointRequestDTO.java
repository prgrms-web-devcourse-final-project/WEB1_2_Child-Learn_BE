package com.prgrms.ijuju.domain.wallet.dto.request;

import com.prgrms.ijuju.domain.wallet.entity.PointType;
import com.prgrms.ijuju.domain.wallet.entity.TransactionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointRequestDTO {
    
    private Long memberId;
    private Long points;
    private PointType pointType;  
    private TransactionType transactionType;
} 