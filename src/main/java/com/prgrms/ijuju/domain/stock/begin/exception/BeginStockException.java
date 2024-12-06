package com.prgrms.ijuju.domain.stock.begin.exception;

import com.prgrms.ijuju.global.exception.BusinessException;

public class BeginStockException extends BusinessException {
    public BeginStockException(BeginStockErrorCode errorCode) {
        super(errorCode);
    }
}
