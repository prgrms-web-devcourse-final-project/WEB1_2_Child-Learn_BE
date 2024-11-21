package com.prgrms.ijuju.stock.adv.stock.dto;


import lombok.Getter;

import java.util.List;

/**
 * 해당 DTO 는 Finnhub API 와 소통하기 위한 DTO 로, 저희 프론트 측과는 관계 없습니다. 내부적으로만 사용되는 dto 입니다.
 */

@Getter
public class PolygonCandleResponse {
    private boolean adjusted;
    private int queryCount;
    private String status;
    private String ticker;
    private List<PolygonCandleResult> results;

}
