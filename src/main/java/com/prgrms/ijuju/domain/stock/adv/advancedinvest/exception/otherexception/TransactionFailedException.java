package com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.otherexception;

import com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.AdvancedInvestErrorCode;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.exception.AdvancedInvestException;

public class TransactionFailedException extends AdvancedInvestException {
    public TransactionFailedException() {
        super(AdvancedInvestErrorCode.TRANSACTION_FAILED);
    }
}
