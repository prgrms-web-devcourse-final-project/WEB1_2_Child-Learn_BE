package com.prgrms.ijuju.domain.minigame.flipcard.exception;

import com.prgrms.ijuju.global.exception.BusinessException;

public class FlipCardException extends BusinessException {
    public FlipCardException(FlipCardErrorCode errorCode) {
        super(errorCode);
    }
}
