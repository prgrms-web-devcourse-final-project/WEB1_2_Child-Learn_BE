package com.prgrms.ijuju.domain.stock.mid.exception;

public class MidAlreadyBoughtException extends MidStockException {
    public MidAlreadyBoughtException() {
        super(MidStockErrorCode.ALREADY_BOUGHT);
    }
}
