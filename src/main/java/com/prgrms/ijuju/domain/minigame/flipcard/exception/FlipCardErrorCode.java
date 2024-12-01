package com.prgrms.ijuju.domain.minigame.flipcard.exception;

import org.springframework.http.HttpStatus;

public enum FlipCardErrorCode {

    DIFFICULTY_NOT_FOUND("FLIP_CARD_001", "[플립카드] 해당 난이도가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    MEMBER_NOT_FOUND("FLIP_CARD_002", "[플립카드] 해당 회원이 존재하지 않습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus status;

    FlipCardErrorCode(String code, String message, HttpStatus status) {
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
