package com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.gameexception;

import com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.AdvancedInvestErrorCode;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.AdvancedInvestException;

public class GameAlreadyStartedException extends AdvancedInvestException {
    public GameAlreadyStartedException() {
        super(AdvancedInvestErrorCode.GAME_ALREADY_STARTED);
    }
}