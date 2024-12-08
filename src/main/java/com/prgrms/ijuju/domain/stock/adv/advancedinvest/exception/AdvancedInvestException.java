package com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception;

import com.prgrms.ijuju.global.exception.BusinessException;
import com.prgrms.ijuju.global.exception.ErrorCode;

public class AdvancedInvestException extends BusinessException {
    public AdvancedInvestException(ErrorCode errorCode) {
        super(errorCode);
    }
}