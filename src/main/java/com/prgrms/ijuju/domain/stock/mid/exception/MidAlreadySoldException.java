package com.prgrms.ijuju.domain.stock.mid.exception;

public class MidAlreadySoldException extends MidStockException {
    public MidAlreadySoldException() {
        super(MidStockErrorCode.ALREADY_SOLD);
    }
}
