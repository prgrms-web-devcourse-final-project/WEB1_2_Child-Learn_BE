package com.prgrms.ijuju.domain.avatar.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ItemErrorCode {

    ITEM_NOT_FOUND("CH001", "존재하지 않는 아이템입니다.", HttpStatus.NOT_FOUND),
    NOT_ENOUGH_COINS("CH002", "코인이 부족합니다.", HttpStatus.BAD_REQUEST),
    INAVALID_ITEM_CATEGORY("CH003", "유효하지 않은 아이템 카테고리입니다.", HttpStatus.BAD_REQUEST),
    ITEM_IS_ALREADY_REGISTED("CH004", "이미 등록된 아이템입니다", HttpStatus.BAD_REQUEST),
    ITEM_NOT_REGISTERED("CH005", "아이템 등록에 실패했습니다.", HttpStatus.BAD_REQUEST),
    ITEM_IS_ALREADY_PURCHASED("CH006", "이미 구매한 아이템입니다", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ItemErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

}
