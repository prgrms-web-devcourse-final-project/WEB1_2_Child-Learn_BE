package com.prgrms.ijuju.domain.stock.adv.advstock.exception.stockexception;

import com.prgrms.ijuju.domain.stock.adv.advstock.exception.AdvStockErrorCode;
import com.prgrms.ijuju.global.exception.BusinessException;

public class ReferenceDataNotFoundException extends BusinessException {
    public ReferenceDataNotFoundException() {
        super(AdvStockErrorCode.REFERENCE_DATA_NOT_FOUND);
    }
}
