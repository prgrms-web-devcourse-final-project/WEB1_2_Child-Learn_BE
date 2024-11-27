package com.prgrms.ijuju.domain.friend.exception;

import org.springframework.http.HttpStatus;

public enum FriendException {
    
    FRIEND_REQUEST_NOT_FOUND("FRIEND_REQUEST_NOT_FOUND", "친구 요청을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FRIEND_ALREADY_EXISTS("FRIEND_ALREADY_EXISTS", "이미 친구 관계입니다.", HttpStatus.CONFLICT),
    FRIEND_REQUEST_ALREADY_SENT("FRIEND_REQUEST_ALREADY_SENT", "이미 친구 요청을 보냈습니다.", HttpStatus.CONFLICT),
    FRIEND_NOT_FOUND("FRIEND_NOT_FOUND", "친구를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FRIEND_REQUEST_REJECTED("FRIEND_REQUEST_REJECTED", "친구 요청이 거절되었습니다.", HttpStatus.BAD_REQUEST),
    FRIEND_REQUEST_ACCEPTED("FRIEND_REQUEST_ACCEPTED", "친구 요청이 수락되었습니다.", HttpStatus.OK),
    FRIEND_REMOVED("FRIEND_REMOVED", "친구가 삭제되었습니다.", HttpStatus.OK),
    FRIEND_LIST_EMPTY("FRIEND_LIST_EMPTY", "친구 목록이 비어 있습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    FriendException(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public FriendTaskException getFriendTaskException() {
        return new FriendTaskException(this.message, this.httpStatus.value());
    }
}