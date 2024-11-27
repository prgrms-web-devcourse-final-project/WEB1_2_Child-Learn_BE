package com.prgrms.ijuju.domain.friend.exception;

public class FriendTaskException extends RuntimeException {
    private final String message;
    private final int statusCode;

    public FriendTaskException(String message, int statusCode) {
        super(message);
        this.message = message;
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }
} 