package com.prgrms.ijuju.domain.wallet.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import com.prgrms.ijuju.global.exception.ErrorCode;

@Getter
public enum WalletErrorCode implements ErrorCode {

    // 회원
    MEMBER_NOT_FOUND("MEMBER_001", "해당 회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    MEMBER_INVALID_ID("MEMBER_002", "회원 ID가 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    MEMBER_UNAUTHORIZED("MEMBER_003", "권한이 없는 회원입니다.", HttpStatus.FORBIDDEN),

    // 지갑
    WALLET_NOT_FOUND("WALLET_001", "지갑을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    WALLET_ALREADY_EXISTS("WALLET_002", "이미 지갑이 존재합니다.", HttpStatus.CONFLICT),
    WALLET_CREATION_FAILED("WALLET_003", "지갑 생성에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    WALLET_UPDATE_FAILED("WALLET_004", "지갑 업데이트에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // 거래
    TRANSACTION_DUPLICATE("TRX_001", "중복된 거래입니다.", HttpStatus.CONFLICT),
    TRANSACTION_INVALID_TYPE("TRX_002", "유효하지 않은 거래 유형입니다.", HttpStatus.BAD_REQUEST),
    TRANSACTION_HISTORY_NOT_FOUND("TRX_003", "거래 내역을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    TRANSACTION_DATE_INVALID("TRX_004", "유효하지 않은 거래 조회 기간입니다.", HttpStatus.BAD_REQUEST),
    TRANSACTION_AMOUNT_INVALID("TRX_005", "유효하지 않은 거래 금액입니다.", HttpStatus.BAD_REQUEST),
    TRANSACTION_FAILED("TRX_006", "거래 처리에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // 포인트
    POINT_INSUFFICIENT("POINT_001", "포인트가 부족합니다.", HttpStatus.BAD_REQUEST),
    POINT_AMOUNT_INVALID("POINT_002", "유효하지 않은 포인트 금액입니다.", HttpStatus.BAD_REQUEST),
    POINT_UPDATE_FAILED("POINT_003", "포인트 업데이트에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    POINT_NEGATIVE_NOT_ALLOWED("POINT_004", "포인트는 음수가 될 수 없습니다.", HttpStatus.BAD_REQUEST),
    POINT_EXCEED_LIMIT("POINT_005", "포인트 한도를 초과했습니다.", HttpStatus.BAD_REQUEST),

    // 코인
    COIN_INSUFFICIENT("COIN_001", "코인이 부족합니다.", HttpStatus.BAD_REQUEST),
    COIN_UPDATE_FAILED("COIN_002", "코인 업데이트에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    COIN_NEGATIVE_NOT_ALLOWED("COIN_003", "코인은 음수가 될 수 없습니다.", HttpStatus.BAD_REQUEST),
    COIN_EXCEED_LIMIT("COIN_004", "코인 한도를 초과했습니다.", HttpStatus.BAD_REQUEST),

    // 환전
    EXCHANGE_MINIMUM_AMOUNT("EXCHANGE_001", "환전은 최소 100포인트부터 가능합니다.", HttpStatus.BAD_REQUEST),
    EXCHANGE_INVALID_UNIT("EXCHANGE_002", "환전은 100포인트 단위로만 가능합니다.", HttpStatus.BAD_REQUEST),
    EXCHANGE_REQUEST_INVALID("EXCHANGE_003", "유효하지 않은 환전 요청입니다.", HttpStatus.BAD_REQUEST),
    EXCHANGE_HISTORY_NOT_FOUND("EXCHANGE_004", "환전 내역을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    EXCHANGE_LIMIT_EXCEEDED("EXCHANGE_005", "일일 환전 한도를 초과했습니다.", HttpStatus.BAD_REQUEST),
    EXCHANGE_FAILED("EXCHANGE_006", "환전 처리에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // 출석
    ATTENDANCE_ALREADY_CHECKED("ATTENDANCE_001", "오늘은 이미 출석체크를 하셨습니다.", HttpStatus.CONFLICT),
    ATTENDANCE_NOT_FOUND("ATTENDANCE_002", "출석체크 내역을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ATTENDANCE_TIME_INVALID("ATTENDANCE_003", "출석체크 가능 시간이 아닙니다.", HttpStatus.BAD_REQUEST),
    ATTENDANCE_UPDATE_FAILED("ATTENDANCE_004", "출석체크 처리에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // 게임
    GAME_TYPE_INVALID("GAME_001", "유효하지 않은 게임 유형입니다.", HttpStatus.BAD_REQUEST),
    GAME_POINT_NOT_FOUND("GAME_002", "게임 포인트 내역을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    GAME_POINT_INVALID("GAME_003", "유효하지 않은 게임 포인트입니다.", HttpStatus.BAD_REQUEST),
    GAME_DAILY_LIMIT_EXCEEDED("GAME_004", "일일 게임 참여 횟수를 초과했습니다.", HttpStatus.BAD_REQUEST),
    GAME_PROCESSING_FAILED("GAME_005", "게임 처리에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // 시스템 예외
    SYSTEM_ERROR("SYS_001", "시스템 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_REQUEST("SYS_002", "유효하지 않은 요청입니다.", HttpStatus.BAD_REQUEST),
    REALTIME_SYNC_FAILED("SYS_003", "실시간 동기화에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    DATABASE_ERROR("SYS_004", "데이터베이스 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    WalletErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
