package com.prgrms.ijuju.stock.adv.stock.dto;


import java.util.List;

/**
 * 해당 DTO 는 Finnhub API 와 소통하기 위한 DTO 로, 저희 프론트 측과는 관계 없습니다. 내부적으로만 사용되는 dto 입니다.
 */
public class PolygonCandleResponse {
    private List<Double> c; // 종가
    private List<Double> h; // 고가
    private List<Double> l; // 저가
    private List<Double> o; // 시가
    private List<Long> t;   // 타임스탬프
    private List<Long> v;   // 거래량


    //getter 이름 변경을 위해서 작성
    public List<Double> getClosePrices() {
        return c;
    }

    public List<Double> getHighPrices() {
        return h;
    }

    public List<Double> getLowPrices() {
        return l;
    }

    public List<Double> getOpenPrices() {
        return o;
    }

    public List<Long> getTimestamps() {
        return t;
    }

    public List<Long> getVolumes() {
        return v;
    }
}
