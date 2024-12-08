package com.prgrms.ijuju.domain.notification.exception;

import com.prgrms.ijuju.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum NotificationErrorCode implements ErrorCode {
    NOTIFICATION_NOT_FOUND("NOTIFICATION_001", "해당 알림이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    SENDER_NOT_FOUND("NOTIFICATION_002", "발신자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    RECEIVER_NOT_FOUND("NOTIFICATION_003", "수신자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NOTIFICATION_ACCESS_DENIED("NOTIFICATION_004", "알림에 권한이 없습니다.", HttpStatus.FORBIDDEN),
    SSE_SEND_ERROR("NOTIFICATION_005", "SSE 전송 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    NotificationErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.httpStatus = status;
    }
}
