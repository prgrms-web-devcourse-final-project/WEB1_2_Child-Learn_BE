package com.prgrms.ijuju.domain.stock.mid.exception;

public class MidPointsNotEnoughException extends MidStockException{
    public MidPointsNotEnoughException() {
        super(MidStockErrorCode.MONEY_NOT_ENOUGH);
    }
}
