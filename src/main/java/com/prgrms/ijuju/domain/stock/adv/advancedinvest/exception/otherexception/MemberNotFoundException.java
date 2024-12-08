package com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.otherexception;

import com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.AdvancedInvestErrorCode;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.AdvancedInvestException;

public class MemberNotFoundException extends AdvancedInvestException {
    public MemberNotFoundException() {
        super(AdvancedInvestErrorCode.MEMBER_NOT_FOUND);
    }
}
