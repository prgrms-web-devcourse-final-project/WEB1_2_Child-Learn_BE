package com.prgrms.ijuju.domain.stock.adv.stockrecord.exception.stockrecordexception;

import com.prgrms.ijuju.domain.stock.adv.stockrecord.exception.StockRecordErrorCode;
import com.prgrms.ijuju.global.exception.BusinessException;

public class MemberNotFoundException extends BusinessException {
    public MemberNotFoundException() {
        super(StockRecordErrorCode.MEMBER_NOT_FOUND);
    }
}