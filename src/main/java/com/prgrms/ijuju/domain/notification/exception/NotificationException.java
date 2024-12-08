package com.prgrms.ijuju.domain.notification.exception;

import com.prgrms.ijuju.global.exception.BusinessException;

public class NotificationException extends BusinessException {
    public NotificationException(NotificationErrorCode errorCode) {
        super(errorCode);
    }
}
