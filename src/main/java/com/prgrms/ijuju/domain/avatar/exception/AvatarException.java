package com.prgrms.ijuju.domain.avatar.exception;

public class AvatarException extends RuntimeException {

    private final AvatarErrorCode errorCode;

    public AvatarException(AvatarErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public AvatarErrorCode getErrorCode() {
        return errorCode;
    }
}
