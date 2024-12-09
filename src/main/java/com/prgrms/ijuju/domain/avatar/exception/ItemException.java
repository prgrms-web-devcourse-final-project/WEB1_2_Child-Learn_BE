package com.prgrms.ijuju.domain.avatar.exception;

import com.prgrms.ijuju.global.exception.BusinessException;

public class ItemException extends BusinessException {

    public ItemException(ItemErrorCode errorCode) {
        super(errorCode);
    }
}
