package com.prgrms.ijuju.domain.stock.mid.exception;

public class MidStockNotFoundException extends  MidStockException{
    public MidStockNotFoundException() {
        super(MidStockErrorCode.STOCK_NOT_FOUND);
    }
}
