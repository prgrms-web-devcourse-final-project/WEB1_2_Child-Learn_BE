package com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.stockexception;

import com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.AdvancedInvestErrorCode;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.AdvancedInvestException;

public class InvalidQuantityException extends AdvancedInvestException {
    public InvalidQuantityException() {
        super(AdvancedInvestErrorCode.INVALID_QUANTITY);
    }
}
