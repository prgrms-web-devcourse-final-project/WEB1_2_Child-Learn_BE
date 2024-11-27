package com.prgrms.ijuju.domain.minigame.flipcard.exception;

public class FlipCardException extends RuntimeException {
    private final FlipCardErrorCode errorCode;

    public FlipCardException(String message, FlipCardErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public FlipCardException(FlipCardErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public FlipCardErrorCode getErrorCode() {
        return errorCode;
    }
}
