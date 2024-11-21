package com.prgrms.ijuju.domain.stock.mid.exception;

public class MemberNotFoundException extends MidStockException{
    public MemberNotFoundException() {
        super(MidStockErrorCode.MEMBER_NOT_FOUND);
    }
}
