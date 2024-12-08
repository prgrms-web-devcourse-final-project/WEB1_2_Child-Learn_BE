package com.prgrms.ijuju.domain.stock.adv.advstock.exception.stockexception;

import com.prgrms.ijuju.domain.stock.adv.advstock.exception.AdvStockErrorCode;
import com.prgrms.ijuju.global.exception.BusinessException;

public class LiveDataNotFoundException extends BusinessException {
    public LiveDataNotFoundException() {
        super(AdvStockErrorCode.LIVE_DATA_NOT_FOUND);
    }
}