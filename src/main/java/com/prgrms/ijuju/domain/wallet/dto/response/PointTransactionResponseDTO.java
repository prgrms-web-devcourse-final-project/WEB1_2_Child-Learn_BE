package com.prgrms.ijuju.domain.wallet.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PointTransactionResponseDTO { 
    
    private Long id;
    private Long memberId;
    private String transactionType;
    private Long points;
    private String pointType;
    private String subType;
    private String description;
    private LocalDateTime createdAt;
}
