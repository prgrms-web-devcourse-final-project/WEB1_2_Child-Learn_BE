package com.prgrms.ijuju.domain.chat.exception;

import com.prgrms.ijuju.global.exception.BusinessException;

public class ChatException extends BusinessException {

    public ChatException(ChatErrorCode errorCode) {
        super(errorCode);
    }
}
