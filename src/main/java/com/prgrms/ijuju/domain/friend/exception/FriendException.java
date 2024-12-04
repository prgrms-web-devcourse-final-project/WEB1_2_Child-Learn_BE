package com.prgrms.ijuju.domain.friend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum FriendException {
    
    // 친구 관련 예외
    FRIEND_REQUEST_SENT("FRIEND_001", "친구 요청이 성공적으로 보내졌습니다.", HttpStatus.OK),
    FRIEND_REQUEST_CANCELLED("FRIEND_002", "친구 요청이 취소되었습니다.", HttpStatus.OK),
    FRIEND_ALREADY_EXISTS("FRIEND_003", "이미 친구 관계입니다.", HttpStatus.CONFLICT),
    FRIEND_NOT_FOUND("FRIEND_004", "친구를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FRIEND_REQUEST_NOT_FOUND("FRIEND_005", "친구 요청을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FRIEND_REQUEST_ACCEPTED("FRIEND_006", "친구 요청이 수락되었습니다.", HttpStatus.OK),
    FRIEND_REQUEST_REJECTED("FRIEND_007", "친구 요청이 거절되었습니다.", HttpStatus.BAD_REQUEST),
    FRIEND_REQUEST_ALREADY_SENT("FRIEND_008", "이미 친구 요청을 보냈습니다.", HttpStatus.CONFLICT),
    REQUEST_ALREADY_PROCESSED("FRIEND_009", "이미 처리된 요청입니다.", HttpStatus.BAD_REQUEST),
    FRIEND_LIST_EMPTY("FRIEND_010", "친구 목록이 비어 있습니다.", HttpStatus.NOT_FOUND),
    FRIEND_NOT_FRIEND("FRIEND_011", "친구 관계가 아닙니다.", HttpStatus.BAD_REQUEST),
    FRIEND_REMOVED("FRIEND_012", "친구가 삭제되었습니다.", HttpStatus.OK),
    SELF_REQUEST_NOT_ALLOWED("FRIEND_013", "자기 자신에게 친구 요청을 보낼 수 없습니다.", HttpStatus.BAD_REQUEST),
    FRIEND_REQUEST_NOT_AUTHORIZED("FRIEND_014", "해당 친구 요청에 대한 권한이 없습니다.", HttpStatus.FORBIDDEN),

    // 친구 요청 관련 예외
    
    // 시스템 예외
    INVALID_REQUEST("FRIEND_015", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    SYSTEM_ERROR("FRIEND_016", "시스템 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    
    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    FriendException(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public FriendTaskException toException() {
        return new FriendTaskException(this.code, this.message, this.httpStatus.value());
    }
}
