package com.prgrms.ijuju.stock.adv.stock.dto;

import lombok.Getter;

/**
 * 해당 DTO 는 Finnhub API 와 소통하기 위한 DTO 로, 저희 프론트 측과는 관계 없습니다. 내부적으로만 사용되는 dto 입니다.
 */

@Getter
public class PolygonCandleResult {
    private double c; // 종가
    private double h; // 고가
    private double l; // 저가
    private double o; // 시가
    private long t;   // 타임스탬프
    private long v;   // 거래량
    private double vw; // 가중평균가
}