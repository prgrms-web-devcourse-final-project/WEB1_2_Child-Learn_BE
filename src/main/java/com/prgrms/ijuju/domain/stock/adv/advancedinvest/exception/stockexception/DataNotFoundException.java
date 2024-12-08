package com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.stockexception;

import com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.AdvancedInvestException;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.AdvancedInvestErrorCode;

public class DataNotFoundException extends AdvancedInvestException {
    public DataNotFoundException() {
        super(AdvancedInvestErrorCode.DATA_NOT_FOUND);
    }
}
