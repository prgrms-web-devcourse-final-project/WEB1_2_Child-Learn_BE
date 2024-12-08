package com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.gameexception;

import com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.AdvancedInvestErrorCode;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.AdvancedInvestException;

public class InvalidGameTimeException extends AdvancedInvestException {
    public InvalidGameTimeException() {
        super(AdvancedInvestErrorCode.INVALID_GAME_TIME);
    }
}