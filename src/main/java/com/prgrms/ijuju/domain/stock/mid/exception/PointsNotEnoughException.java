package com.prgrms.ijuju.domain.stock.mid.exception;

public class PointsNotEnoughException extends MidStockException{
    public PointsNotEnoughException() {
        super(MidStockErrorCode.MONEY_NOT_ENOUGH);
    }
}
