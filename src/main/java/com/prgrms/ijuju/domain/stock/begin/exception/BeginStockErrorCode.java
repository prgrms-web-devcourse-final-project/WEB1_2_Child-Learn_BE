package com.prgrms.ijuju.domain.stock.begin.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BeginStockErrorCode {

    STOCK_NOT_FOUND("BEGIN_STOCK_001", "주식 데이터를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ALREADY_PLAYED_GAME("BEGIN_STOCK_002", "이미 오늘의 게임을 진행하였습니다.", HttpStatus.BAD_REQUEST),
    QUIZ_NOT_FOUND("BEGIN_STOCK_003", "오늘의 퀴즈를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
