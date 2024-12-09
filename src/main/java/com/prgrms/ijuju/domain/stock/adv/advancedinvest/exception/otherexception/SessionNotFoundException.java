package com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.otherexception;

import com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.AdvancedInvestErrorCode;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.AdvancedInvestException;

public class SessionNotFoundException extends AdvancedInvestException {
    public SessionNotFoundException() {
        super(AdvancedInvestErrorCode.SESSION_NOT_FOUND);
    }
}
