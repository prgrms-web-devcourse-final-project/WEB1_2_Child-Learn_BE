package com.prgrms.ijuju.domain.chat.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ChatException {
    
    // 채팅방 관련 예외
    CHATROOM_NOT_FOUND("CH001", "존재하지 않는 채팅방입니다.", HttpStatus.NOT_FOUND),
    CHATROOM_ALREADY_EXISTS("CH002", "이미 존재하는 채팅방입니다.", HttpStatus.BAD_REQUEST),
    CHATROOM_ACCESS_DENIED("CH003", "채팅방 접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    CHATROOM_DELETED("CH004", "삭제된 채팅방입니다.", HttpStatus.BAD_REQUEST),
    
    // 메시지 관련 예외
    MESSAGE_NOT_FOUND("CH101", "존재하지 않는 메시지입니다.", HttpStatus.NOT_FOUND),
    MESSAGE_DELETION_TIMEOUT("CH102", "메시지 삭제는 작성 후 5분 이내에만 가능합니다.", HttpStatus.BAD_REQUEST),
    MESSAGE_DELETION_DENIED("CH103", "메시지 삭제 권한이 없습니다.", HttpStatus.FORBIDDEN),
    MESSAGE_EMPTY("CH104", "메시지 내용이 비어있습니다.", HttpStatus.BAD_REQUEST),
    MESSAGE_TOO_LONG("CH105", "메시지가 너무 깁니다. (최대 1000자)", HttpStatus.BAD_REQUEST),
    
    // 사용자 관련 예외
    USER_NOT_FOUND("CH201", "존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND),
    USER_ACCESS_DENIED("CH202", "접근 권한이 없는 사용자입니다.", HttpStatus.FORBIDDEN),
    USER_SELF_CHAT("CH203", "자기 자신과는 채팅할 수 없습니다.", HttpStatus.BAD_REQUEST),
    USER_BLOCKED("CH204", "차단된 사용자와는 채팅할 수 없습니다.", HttpStatus.FORBIDDEN),
    
    // 이미지 관련 예외
    IMAGE_UPLOAD_FAILED("CH301", "이미지 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    IMAGE_SIZE_EXCEEDED("CH302", "이미지 크기가 너무 큽니다. (최대 5MB)", HttpStatus.BAD_REQUEST),
    IMAGE_FORMAT_INVALID("CH303", "지원하지 않는 이미지 형식입니다. (jpg, png만 가능)", HttpStatus.BAD_REQUEST),
    
    // 시스템 예외
    INTERNAL_ERROR("CH901", "내부 서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    REDIS_CONNECTION_ERROR("CH902", "메시지 서버 연결에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    DATABASE_ERROR("CH903", "데이터베이스 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ChatException(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public ChatTaskException toException() {
        return new ChatTaskException(this.code, this.message, this.httpStatus.value());
    }
}
