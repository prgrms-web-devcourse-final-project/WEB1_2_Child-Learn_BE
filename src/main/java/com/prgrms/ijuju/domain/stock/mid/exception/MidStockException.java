package com.prgrms.ijuju.domain.stock.mid.exception;

import com.prgrms.ijuju.global.exception.BusinessException;

public class MidStockException extends BusinessException {
    public MidStockException(MidStockErrorCode errorCode) {
        super(errorCode);
    }
}
