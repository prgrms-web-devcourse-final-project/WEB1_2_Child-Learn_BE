package com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception;

import com.prgrms.ijuju.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AdvancedInvestErrorCode implements ErrorCode {
    GAME_NOT_FOUND("ADV_INVEST_001", "[고급]게임을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    MEMBER_NOT_FOUND("ADV_INVEST_002", "[고급]멤버를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ALREADY_PLAYED_TODAY("ADV_INVEST_003", "[고급]오늘 이미 게임을 진행했습니다.", HttpStatus.BAD_REQUEST),
    INVALID_GAME_TIME("ADV_INVEST_004", "[고급]유효하지 않은 게임 실행 시간입니다.", HttpStatus.BAD_REQUEST),
    STOCK_NOT_FOUND("ADV_INVEST_005", "[고급]주식을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_QUANTITY("ADV_INVEST_006", "[고급]수량은 0보다 커야 합니다.", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_STOCK("ADV_INVEST_007", "[고급]보유 주식 수량이 부족합니다.", HttpStatus.BAD_REQUEST),
    DATA_NOT_INITIALIZED("ADV_INVEST_008", "[고급]데이터가 초기화되지 않았습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    SESSION_NOT_FOUND("ADV_INVEST_009", "[고급]세션을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    DATA_NOT_FOUND("ADV_INVEST_008", "필요한 데이터를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    TRANSACTION_FAILED("ADV_INVEST_010", "[고급]거래 처리에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    AdvancedInvestErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}