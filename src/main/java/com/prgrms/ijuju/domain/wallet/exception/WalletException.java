package com.prgrms.ijuju.domain.wallet.exception;

import com.prgrms.ijuju.global.exception.BusinessException;

public class WalletException extends BusinessException {
    
    public WalletException(WalletErrorCode errorCode) {
        super(errorCode);
    }
} 
