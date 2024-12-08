package com.prgrms.ijuju.domain.stock.adv.stockrecord.exception.stockrecordexception;

import com.prgrms.ijuju.domain.stock.adv.stockrecord.exception.StockRecordErrorCode;
import com.prgrms.ijuju.global.exception.BusinessException;

public class RecordSaveFailedException extends BusinessException {
    public RecordSaveFailedException() {
        super(StockRecordErrorCode.RECORD_SAVE_FAILED);
    }
}