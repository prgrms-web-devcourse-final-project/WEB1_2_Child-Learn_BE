package com.prgrms.ijuju.domain.wallet.dto.request;

import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.prgrms.ijuju.domain.wallet.entity.TransactionType;
import com.prgrms.ijuju.domain.wallet.entity.PointType;
import com.prgrms.ijuju.domain.wallet.entity.StockType;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockPointRequestDTO {
    
    private Long memberId;
    private Long points;
    private PointType pointType;
    private TransactionType transactionType;
    private StockType stockType;
    private String stockName;
}
