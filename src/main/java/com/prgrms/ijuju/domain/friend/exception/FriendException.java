package com.prgrms.ijuju.domain.friend.exception;

import com.prgrms.ijuju.global.exception.BusinessException;

public class FriendException extends BusinessException {

    public FriendException(FriendErrorCode errorCode) {
        super(errorCode);
    }
}
