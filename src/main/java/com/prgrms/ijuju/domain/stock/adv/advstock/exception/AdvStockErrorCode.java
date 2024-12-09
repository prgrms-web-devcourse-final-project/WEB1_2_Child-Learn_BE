package com.prgrms.ijuju.domain.stock.adv.advstock.exception;

import com.prgrms.ijuju.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AdvStockErrorCode implements ErrorCode {
    REFERENCE_DATA_NOT_FOUND("ADV_STOCK_001", "[고급] 참조 데이터를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    LIVE_DATA_NOT_FOUND("ADV_STOCK_002", "[고급] 라이브 데이터를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_TRADE_HOUR("ADV_STOCK_003", "[고급] 유효하지 않은 거래 시간입니다.", HttpStatus.BAD_REQUEST),
    EMPTY_STOCK_DATA("ADV_STOCK_004", "[고급] 저장할 주식 데이터가 비어있습니다.", HttpStatus.BAD_REQUEST),
    STOCK_DATA_MISMATCH("ADV_STOCK_005", "[고급] 주식 데이터의 크기가 일치하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    STOCK_SAVE_FAILED("ADV_STOCK_006", "[고급] 주식 데이터 저장에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    AdvStockErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}