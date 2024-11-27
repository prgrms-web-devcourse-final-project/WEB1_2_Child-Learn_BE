package com.prgrms.ijuju.domain.stock.adv.advancedinvest.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WebSocketRequestDto {
    private String action; // START_GAME, PAUSE_GAME, RESUME_GAME, END_GAME, LIVE_DATA 등
    private Long advId;
    private String symbol;
    private int hour;
}
