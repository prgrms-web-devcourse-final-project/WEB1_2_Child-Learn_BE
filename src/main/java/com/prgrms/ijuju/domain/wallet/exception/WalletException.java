package com.prgrms.ijuju.domain.wallet.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum WalletException {

    MEMBER_NOT_FOUND("MEMBER_001", "해당 회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_MEMBER_ID("MEMBER_002", "회원 ID가 유효하지 않습니다.", HttpStatus.BAD_REQUEST),

    WALLET_NOT_FOUND("WALLET_001", "지갑을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    INSUFFICIENT_POINTS("POINT_001", "포인트가 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_AMOUNT("POINT_002", "금액이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    POINT_UPDATE_FAILED("POINT_003", "포인트 업데이트에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    COIN_UPDATE_FAILED("COIN_001", "코인 업데이트에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    EXCHANGE_MINIMUM("EXCHANGE_001", "환전은 최소 100포인트부터 가능합니다.", HttpStatus.BAD_REQUEST),
    EXCHANGE_UNIT("EXCHANGE_002", "환전은 100포인트 단위로만 가능합니다.", HttpStatus.BAD_REQUEST),
    INVALID_EXCHANGE_REQUEST("EXCHANGE_003", "유효하지 않은 환전 요청입니다.", HttpStatus.BAD_REQUEST),
    EXCHANGE_DETAILS_NOT_FOUND("EXCHANGE_004", "환전 내역을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    INVALID_STOCK_STATUS("STOCK_001", "유효하지 않은 주식 상태입니다.", HttpStatus.BAD_REQUEST),
    INVALID_STOCK_TYPE("STOCK_002", "유효하지 않은 주식 타입입니다.", HttpStatus.BAD_REQUEST),
    STOCK_POINT_NOT_FOUND("STOCK_003", "주식 포인트를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    INVALID_GAME_TYPE("GAME_001", "유효하지 않은 게임 타입입니다.", HttpStatus.BAD_REQUEST),
    GAME_POINT_NOT_FOUND("GAME_002", "게임 포인트를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_GAME_POINT("GAME_003", "유효하지 않은 게임 포인트 요청입니다.", HttpStatus.BAD_REQUEST),
    DUPLICATE_TRANSACTION("TRANSACTION_001", "중복된 거래입니다.", HttpStatus.CONFLICT),
    INVALID_TRANSACTION_TYPE("TRANSACTION_002", "유효하지 않은 거래 유형입니다.", HttpStatus.BAD_REQUEST),
    POINT_HISTORY_NOT_FOUND("TRANSACTION_003", "포인트 거래 내역을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    WEEKLY_DATE_INVALID("TRANSACTION_004", "유효하지 않은 주간 조회 기간입니다.", HttpStatus.BAD_REQUEST),
    
    ATTENDANCE_ALREADY_CHECKED("ATTENDANCE_001", "오늘은 이미 출석체크를 하셨습니다.", HttpStatus.CONFLICT),
    ATTENDANCE_NOT_FOUND("ATTENDANCE_002", "출석체크 내역을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    INVALID_REQUEST("REQUEST_001", "유효하지 않은 요청입니다.", HttpStatus.BAD_REQUEST),
    REALTIME_SYNC_FAILED("SYNC_001", "실시간 업데이트에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    
    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    WalletException(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
