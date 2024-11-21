package com.prgrms.ijuju.domain.stock.mid.exception;

public class StockNotFoundException extends  MidStockException{
    public StockNotFoundException() {
        super(MidStockErrorCode.STOCK_NOT_FOUND);
    }
}
