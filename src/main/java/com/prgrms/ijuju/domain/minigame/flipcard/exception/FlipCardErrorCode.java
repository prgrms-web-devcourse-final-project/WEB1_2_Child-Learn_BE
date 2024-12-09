package com.prgrms.ijuju.domain.minigame.flipcard.exception;

import com.prgrms.ijuju.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum FlipCardErrorCode implements ErrorCode {

    DIFFICULTY_NOT_FOUND("FLIP_CARD_001", "[플립카드] 해당 난이도가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    MEMBER_NOT_FOUND("FLIP_CARD_002", "[플립카드] 해당 회원이 존재하지 않습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    FlipCardErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.httpStatus = status;
    }
}
