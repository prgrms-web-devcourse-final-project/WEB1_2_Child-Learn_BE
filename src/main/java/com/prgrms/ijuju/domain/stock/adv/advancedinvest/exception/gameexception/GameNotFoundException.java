package com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.gameexception;

import com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.AdvancedInvestErrorCode;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.AdvancedInvestException;

public class GameNotFoundException extends AdvancedInvestException {
    public GameNotFoundException() {
        super(AdvancedInvestErrorCode.GAME_NOT_FOUND);
    }
}
