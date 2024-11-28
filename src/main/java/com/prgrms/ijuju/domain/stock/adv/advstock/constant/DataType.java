package com.prgrms.ijuju.domain.stock.adv.advstock.constant;

public enum DataType {
    REFERENCE,  //2주전부터 1주 전까지의 데이터
    LIVE,        //1주 전 하루의, 실제 게임에 쓰이는 데이터
    FORECAST    // 1주 전 데이터부터 바로 전날까지, 아티클이 쓰는 데이터
}
