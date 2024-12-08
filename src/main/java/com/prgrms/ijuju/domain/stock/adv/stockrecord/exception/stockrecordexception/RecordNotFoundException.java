package com.prgrms.ijuju.domain.stock.adv.stockrecord.exception.stockrecordexception;

import com.prgrms.ijuju.domain.stock.adv.stockrecord.exception.StockRecordErrorCode;
import com.prgrms.ijuju.global.exception.BusinessException;

public class RecordNotFoundException extends BusinessException {
    public RecordNotFoundException() {
        super(StockRecordErrorCode.RECORD_NOT_FOUND);
    }
}