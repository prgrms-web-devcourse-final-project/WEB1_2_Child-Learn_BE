package com.prgrms.ijuju.domain.stock.mid.exception;

import org.springframework.http.HttpStatus;

public enum MidStockErrorCode {
    MONEY_NOT_ENOUGH("MID_STOCK_001", "[중급] 매수할 포인트가 부족합니다.", HttpStatus.BAD_REQUEST),
    STOCK_NOT_FOUND("MID_STOCK_002", "[중급] 해당 주식이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    MEMBER_NOT_FOUND("MID_STOCK_003", "[중급] 해당 회원이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    PRICE_NOT_FOUND("MID_STOCK_004", "[중급] 오늘의 주식 가격이 아직 생성되지 않았습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus status;

    MidStockErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
