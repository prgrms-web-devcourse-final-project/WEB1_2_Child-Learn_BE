package com.prgrms.ijuju.domain.article.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Trend {
    private String durationType; // "SHORT_TERM", "MID_TERM", "LONG_TERM"
    private String description;  // "UP: 3일, DOWN: 2일"
    private String stockSymbol;  // "AAPL"
}