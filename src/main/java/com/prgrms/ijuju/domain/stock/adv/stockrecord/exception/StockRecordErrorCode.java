package com.prgrms.ijuju.domain.stock.adv.stockrecord.exception;

import com.prgrms.ijuju.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum StockRecordErrorCode implements ErrorCode {
    RECORD_SAVE_FAILED("STOCK_RECORD_001", "[거래 기록] 거래 기록 저장에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    MEMBER_NOT_FOUND("STOCK_RECORD_002", "[거래 기록] 회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    RECORD_NOT_FOUND("STOCK_RECORD_003", "[거래 기록] 거래 기록을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    StockRecordErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}