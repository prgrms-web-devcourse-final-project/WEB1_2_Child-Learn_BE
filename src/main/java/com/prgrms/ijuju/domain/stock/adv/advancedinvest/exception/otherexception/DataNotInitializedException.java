package com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.otherexception;

import com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.AdvancedInvestErrorCode;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.AdvancedInvestException;

public class DataNotInitializedException extends AdvancedInvestException {
    public DataNotInitializedException() {
        super(AdvancedInvestErrorCode.DATA_NOT_INITIALIZED);
    }
}
