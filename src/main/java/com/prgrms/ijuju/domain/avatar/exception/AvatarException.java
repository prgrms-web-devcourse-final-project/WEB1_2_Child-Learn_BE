package com.prgrms.ijuju.domain.avatar.exception;

import com.prgrms.ijuju.global.exception.BusinessException;

public class AvatarException extends BusinessException {
    public AvatarException(AvatarErrorCode errorCode) {
        super(errorCode);
    }
}
