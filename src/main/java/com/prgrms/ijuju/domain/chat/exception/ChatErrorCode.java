package com.prgrms.ijuju.domain.chat.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import com.prgrms.ijuju.global.exception.ErrorCode;

@Getter
public enum ChatErrorCode implements ErrorCode {
    
    // 채팅방 관련 예외
    CHATROOM_NOT_FOUND("CHAT_101", "존재하지 않는 채팅방입니다.", HttpStatus.NOT_FOUND),
    CHATROOM_ALREADY_EXISTS("CHAT_102", "채팅방이 이미 존재합니다.", HttpStatus.BAD_REQUEST),
    CHATROOM_ACCESS_DENIED("CHAT_103", "채팅방 접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    CHATROOM_DELETED("CHAT_104", "삭제된 채팅방입니다.", HttpStatus.BAD_REQUEST),
    CHATROOM_MEMBER_LIMIT("CHAT_105", "채팅방 인원 제한을 초과했습니다.", HttpStatus.BAD_REQUEST),
    CHATROOM_CREATION_FAILED("CHAT_106", "채팅방 생성에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    CHATROOM_ALREADY_DELETED("CHAT_107", "이미 삭제된 채팅방입니다.", HttpStatus.BAD_REQUEST),
    NOT_FRIENDS("CHAT_108", "친구 관계가 아닌 사용자와는 채팅을 할 수 없습니다.", HttpStatus.FORBIDDEN),

    // 메시지 관련 예외
    MESSAGE_NOT_FOUND("CHAT_201", "존재하지 않는 메시지입니다.", HttpStatus.NOT_FOUND),
    MESSAGE_DELETION_TIMEOUT("CHAT_202", "메시지 삭제는 작성 후 5분 이내에만 가능합니다.", HttpStatus.BAD_REQUEST),
    MESSAGE_DELETION_DENIED("CHAT_203", "메시지 삭제 권한이 없습니다.", HttpStatus.FORBIDDEN),
    MESSAGE_EMPTY("CHAT_204", "메시지 내용이 비어있습니다.", HttpStatus.BAD_REQUEST),
    MESSAGE_TOO_LONG("CHAT_205", "메시지가 너무 깁니다. (최대 1000자)", HttpStatus.BAD_REQUEST),
    MESSAGE_SEND_FAILED("CHAT_206", "메시지 전송에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    MESSAGE_INVALID_FORMAT("CHAT_207", "잘못된 메시지 형식입니다.", HttpStatus.BAD_REQUEST),
    MESSAGE_RATE_LIMIT("CHAT_208", "메시지 전송 횟수가 제한을 초과했습니다.", HttpStatus.TOO_MANY_REQUESTS),
    MESSAGE_CONTENT_EMPTY("CHAT_209", "메시지 내용이 비어있습니다.", HttpStatus.BAD_REQUEST),

    // 사용자 관련 예외
    MEMBER_NOT_FOUND("CHAT_301", "존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND),  
    USER_ACCESS_DENIED("CHAT_302", "접근 권한이 없는 사용자입니다.", HttpStatus.FORBIDDEN),
    USER_SELF_CHAT("CHAT_303", "자기 자신과는 채팅할 수 없습니다.", HttpStatus.BAD_REQUEST),
    USER_BLOCKED("CHAT_304", "차단된 사용자와는 채팅할 수 없습니다.", HttpStatus.FORBIDDEN),
    USER_INACTIVE("CHAT_305", "비활성화된 사용자입니다.", HttpStatus.FORBIDDEN),
    USER_CHAT_LIMIT("CHAT_306", "채팅방 생성 한도를 초과했습니다.", HttpStatus.BAD_REQUEST),
    USER_NOT_AUTHORIZED("CHAT_307", "인증되지 않은 사용자입니다.", HttpStatus.UNAUTHORIZED),

    // 이미지 관련 예외
    IMAGE_UPLOAD_FAILED("CHAT_401", "이미지 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    IMAGE_SIZE_EXCEEDED("CHAT_402", "이미지 크기가 너무 큽니다. (최대 5MB)", HttpStatus.BAD_REQUEST),
    IMAGE_FORMAT_INVALID("CHAT_403", "지원하지 않는 이미지 형식입니다. (jpg, png만 가능)", HttpStatus.BAD_REQUEST),
    IMAGE_DOWNLOAD_FAILED("CHAT_404", "이미지 다운로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    IMAGE_PROCESSING_FAILED("CHAT_405", "이미지 처리에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    IMAGE_COUNT_EXCEEDED("CHAT_406", "한 메시지당 이미지 개수 제한을 초과했습니다.", HttpStatus.BAD_REQUEST),
    
    // 웹소켓 관련 예외
    WEBSOCKET_CONNECTION_FAILED("CHAT_501", "웹소켓 연결에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    WEBSOCKET_MESSAGE_FAILED("CHAT_502", "웹소켓 메시지 전송에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    WEBSOCKET_AUTH_FAILED("CHAT_503", "웹소켓 인증에 실패했습니다.", HttpStatus.UNAUTHORIZED),
    WEBSOCKET_DISCONNECTED("CHAT_504", "웹소켓 연결이 종료되었습니다.", HttpStatus.SERVICE_UNAVAILABLE),
    
    // 시스템 예외
    INTERNAL_ERROR("CHAT_901", "내부 서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    REDIS_CONNECTION_ERROR("CHAT_902", "메시지 서버 연결에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    DATABASE_ERROR("CHAT_903", "데이터베이스 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    EXTERNAL_API_ERROR("CHAT_904", "외부 API 호출에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    SERVICE_UNAVAILABLE("CHAT_905", "서비스를 일시적으로 사용할 수 없습니다.", HttpStatus.SERVICE_UNAVAILABLE),
    RATE_LIMIT_EXCEEDED("CHAT_906", "요청 횟수가 제한을 초과했습니다.", HttpStatus.TOO_MANY_REQUESTS),
    FILE_UPLOAD_ERROR("CHAT_907", "파일 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ChatErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
