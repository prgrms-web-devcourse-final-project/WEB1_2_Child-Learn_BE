package com.prgrms.ijuju.domain.stock.mid.exception;

public class MidMemberNotFoundException extends MidStockException{
    public MidMemberNotFoundException() {
        super(MidStockErrorCode.MEMBER_NOT_FOUND);
    }
}
