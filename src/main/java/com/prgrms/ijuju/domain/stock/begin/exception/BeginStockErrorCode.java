package com.prgrms.ijuju.domain.stock.begin.exception;

import com.prgrms.ijuju.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BeginStockErrorCode implements ErrorCode {

    STOCK_NOT_FOUND("BEGIN_STOCK_001", "주식 데이터를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    STOCK_DATA_RETRIEVAL_ERROR("BEGIN_STOCK_002", "주식 데이터 조회 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    QUIZ_NOT_FOUND("BEGIN_STOCK_003", "오늘의 퀴즈를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ALREADY_PLAYED_GAME("BEGIN_STOCK_004", "이미 오늘의 게임을 진행하였습니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    BeginStockErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
