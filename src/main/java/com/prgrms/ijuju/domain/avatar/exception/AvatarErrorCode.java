package com.prgrms.ijuju.domain.avatar.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AvatarErrorCode {

    AVATAR_NOT_FOUND("CH001", "아바타가 존재하지 않습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    AvatarErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
