package com.prgrms.ijuju.domain.stock.adv.advstock.exception.stockexception;

import com.prgrms.ijuju.domain.stock.adv.advstock.exception.AdvStockErrorCode;
import com.prgrms.ijuju.global.exception.BusinessException;

public class StockSaveFailedException extends BusinessException {
    public StockSaveFailedException() {
        super(AdvStockErrorCode.STOCK_SAVE_FAILED);
    }
}