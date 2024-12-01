package com.prgrms.ijuju.global.exception;

import com.prgrms.ijuju.domain.wallet.exception.WalletException;

public class CustomException extends RuntimeException {
    private final WalletException pointException;

    public CustomException(WalletException pointException) {
        super(pointException.getMessage());
        this.pointException = pointException;
    }

    public WalletException getPointException() {
        return pointException;
    }
} 