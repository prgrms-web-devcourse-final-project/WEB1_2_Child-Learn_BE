package com.prgrms.ijuju.domain.friend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import com.prgrms.ijuju.global.exception.ErrorCode;

@Getter
public enum FriendErrorCode implements ErrorCode {

    // 성공 응답
    FRIEND_REQUEST_SENT("FRIEND_001", "친구 요청이 성공적으로 전송되었습니다.", HttpStatus.OK),
    FRIEND_REQUEST_CANCELLED("FRIEND_002", "친구 요청이 취소되었습니다.", HttpStatus.OK), 
    FRIEND_REQUEST_ACCEPTED("FRIEND_003", "친구 요청이 수락되었습니다.", HttpStatus.OK),
    FRIEND_REMOVED("FRIEND_004", "친구 관계가 해제되었습니다.", HttpStatus.OK),

    // 클라이언트 오류
    // - 인증/권한 관련
    FRIEND_REQUEST_NOT_AUTHORIZED("FRIEND_101", "해당 친구 요청에 대한 권한이 없습니다.", HttpStatus.FORBIDDEN),
    SELF_REQUEST_NOT_ALLOWED("FRIEND_102", "자기 자신에게 친구 요청을 보낼 수 없습니다.", HttpStatus.BAD_REQUEST),
    
    // - 리소스 없음
    FRIEND_NOT_FOUND("FRIEND_201", "해당 사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FRIEND_REQUEST_NOT_FOUND("FRIEND_202", "해당 친구 요청을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FRIEND_LIST_EMPTY("FRIEND_203", "친구 목록이 비어있습니다.", HttpStatus.NOT_FOUND),
    
    // - 상태 충돌
    FRIEND_ALREADY_EXISTS("FRIEND_301", "이미 친구 관계입니다.", HttpStatus.CONFLICT),
    FRIEND_REQUEST_ALREADY_SENT("FRIEND_302", "이미 친구 요청을 보냈습니다.", HttpStatus.CONFLICT),
    REQUEST_ALREADY_PROCESSED("FRIEND_303", "이미 처리된 요청입니다.", HttpStatus.BAD_REQUEST),
    
    // - 유효성 검증
    FRIEND_NOT_FRIEND("FRIEND_401", "친구 관계가 아닙니다.", HttpStatus.BAD_REQUEST),
    FRIEND_REQUEST_REJECTED("FRIEND_402", "친구 요청이 거절되었습니다.", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST("FRIEND_403", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),

    // 서버 오류
    SYSTEM_ERROR("FRIEND_501", "시스템 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    FriendErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
