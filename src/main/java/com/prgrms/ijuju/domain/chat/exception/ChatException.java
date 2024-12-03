package com.prgrms.ijuju.domain.chat.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ChatException {
    
    // 채팅방 관련 에러
    CHATROOM_NOT_FOUND("CH001", "채팅방을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    UNAUTHORIZED_CHATROOM_ACCESS("CH002", "채팅방에 접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    
    // 메시지 관련 에러
    MESSAGE_NOT_FOUND("CH003", "메시지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    MESSAGE_DELETION_TIMEOUT("CH004", "메시지를 삭제할 수 없습니다. (5분 초과)", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_MESSAGE_DELETION("CH005", "메시지 삭제 권한이 없습니다.", HttpStatus.FORBIDDEN),
    UNAUTHORIZED_MESSAGE_SENDING("CH013", "메시지 전송 권한이 없습니다.", HttpStatus.FORBIDDEN),
    
    // 사용자 관련 에러
    USER_NOT_FOUND("CH006", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_USER_ACCESS("CH007", "잘못된 사용자 접근입니다.", HttpStatus.FORBIDDEN),
    
    // 기타 에러
    INVALID_REQUEST("CH008", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("CH009", "서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    
    // 추가 예외
    CHATROOM_ALREADY_EXISTS("CH010", "이미 존재하는 채팅방입니다.", HttpStatus.BAD_REQUEST),
    INVALID_IMAGE_FORMAT("CH011", "지원하지 않는 이미지 형식입니다.", HttpStatus.BAD_REQUEST),
    IMAGE_UPLOAD_FAILED("CH012", "이미지 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR)    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ChatException(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
