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
    FRIEND_LIST_EMPTY("FRIEND_LIST_EMPTY", "친구 목록이 비어 있습니다.", HttpStatus.NOT_FOUND),
    FRIEND_NOT_FRIEND("FRIEND_NOT_FRIEND", "친구 관계가 아닙니다.", HttpStatus.BAD_REQUEST),
    ALREADY_FRIEND("ALREADY_FRIEND", "이미 친구입니다.", HttpStatus.BAD_REQUEST),
    REQUEST_ALREADY_PROCESSED("REQUEST_ALREADY_PROCESSED", "이미 처리된 요청입니다.", HttpStatus.BAD_REQUEST),
    CANNOT_UPDATE_TO_PENDING("CANNOT_UPDATE_TO_PENDING", "대기 상태로 변경할 수 없습니다.", HttpStatus.BAD_REQUEST),
    CANNOT_UPDATE_TO_ACCEPTED("CANNOT_UPDATE_TO_ACCEPTED", "수락 상태로 변경할 수 없습니다.", HttpStatus.BAD_REQUEST),
    CANNOT_UPDATE_TO_REJECTED("CANNOT_UPDATE_TO_REJECTED", "거절 상태로 변경할 수 없습니다.", HttpStatus.BAD_REQUEST),
    CANNOT_UPDATE_TO_READ("CANNOT_UPDATE_TO_READ", "읽음 상태로 변경할 수 없습니다.", HttpStatus.BAD_REQUEST),
    SELF_REQUEST_NOT_ALLOWED("SELF_REQUEST_NOT_ALLOWED", "자기 자신에게 친구 요청을 보낼 수 없습니다.", HttpStatus.BAD_REQUEST),
    FRIEND_REQUEST_NOT_AUTHORIZED("FRIEND_REQUEST_NOT_AUTHORIZED", "해당 친구 요청에 대한 권한이 없습니다.", HttpStatus.FORBIDDEN);
    
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
}
