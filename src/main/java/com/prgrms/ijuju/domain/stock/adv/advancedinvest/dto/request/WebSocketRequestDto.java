package com.prgrms.ijuju.domain.stock.adv.advancedinvest.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class WebSocketRequestDto {
    private String action;
    private Long advId;
    private String stockSymbol;
    private int quantity;
    private BigDecimal points;
    private Long memberId;
}
