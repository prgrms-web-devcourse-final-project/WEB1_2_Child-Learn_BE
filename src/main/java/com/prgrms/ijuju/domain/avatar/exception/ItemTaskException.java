package com.prgrms.ijuju.domain.avatar.exception;

public class ItemTaskException extends RuntimeException {

    private final int statusCode;

    public ItemTaskException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
