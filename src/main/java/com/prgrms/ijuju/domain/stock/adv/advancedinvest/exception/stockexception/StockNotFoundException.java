package com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.stockexception;

import com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.AdvancedInvestErrorCode;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.AdvancedInvestException;

public class StockNotFoundException extends AdvancedInvestException {
    public StockNotFoundException() {
        super(AdvancedInvestErrorCode.STOCK_NOT_FOUND);
    }
}
