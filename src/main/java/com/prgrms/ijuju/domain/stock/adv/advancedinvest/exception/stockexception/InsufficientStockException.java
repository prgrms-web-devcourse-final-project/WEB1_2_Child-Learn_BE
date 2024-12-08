package com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.stockexception;

import com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.AdvancedInvestErrorCode;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.AdvancedInvestException;

public class InsufficientStockException extends AdvancedInvestException {
    public InsufficientStockException() {
        super(AdvancedInvestErrorCode.INSUFFICIENT_STOCK);
    }
}
