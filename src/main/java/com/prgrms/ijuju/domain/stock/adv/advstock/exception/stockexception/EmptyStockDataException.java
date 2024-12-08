package com.prgrms.ijuju.domain.stock.adv.advstock.exception.stockexception;

import com.prgrms.ijuju.domain.stock.adv.advstock.exception.AdvStockErrorCode;
import com.prgrms.ijuju.global.exception.BusinessException;

public class EmptyStockDataException extends BusinessException {
    public EmptyStockDataException() {
        super(AdvStockErrorCode.EMPTY_STOCK_DATA);
    }
}