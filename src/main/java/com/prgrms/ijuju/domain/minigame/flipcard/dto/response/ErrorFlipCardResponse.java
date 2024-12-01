package com.prgrms.ijuju.domain.minigame.flipcard.dto.response;

import com.prgrms.ijuju.domain.minigame.flipcard.exception.FlipCardErrorCode;
import lombok.Getter;

@Getter
public class ErrorFlipCardResponse {

    private final String message;
    private final int status;
    private final String code;

    public ErrorFlipCardResponse(FlipCardErrorCode errorCode) {
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus().value();
        this.code = errorCode.getCode();
    }

    public static ErrorFlipCardResponse of (FlipCardErrorCode errorCode) {
        return new ErrorFlipCardResponse(errorCode);
    }
}
