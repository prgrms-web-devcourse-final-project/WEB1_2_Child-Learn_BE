package com.prgrms.ijuju.domain.stock.adv.advstock.exception.stockexception;

import com.prgrms.ijuju.domain.stock.adv.advstock.exception.AdvStockErrorCode;
import com.prgrms.ijuju.global.exception.BusinessException;

public class InvalidTradeHourException extends BusinessException {
    public InvalidTradeHourException() {
        super(AdvStockErrorCode.INVALID_TRADE_HOUR);
    }
}
