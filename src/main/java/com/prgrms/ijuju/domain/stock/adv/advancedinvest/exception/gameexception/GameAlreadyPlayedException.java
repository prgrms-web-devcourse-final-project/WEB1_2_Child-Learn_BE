package com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.gameexception;

import com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.AdvancedInvestErrorCode;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.AdvancedInvestException;

public class GameAlreadyPlayedException extends AdvancedInvestException {
    public GameAlreadyPlayedException() {
        super(AdvancedInvestErrorCode.ALREADY_PLAYED_TODAY);
    }
}
