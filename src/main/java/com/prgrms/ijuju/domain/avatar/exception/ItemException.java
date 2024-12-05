package com.prgrms.ijuju.domain.avatar.exception;

public class ItemException extends RuntimeException {

    private final ItemErrorCode errorCode;

    public ItemException(ItemErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ItemErrorCode getErrorCode() {
        return errorCode;
    }
}
