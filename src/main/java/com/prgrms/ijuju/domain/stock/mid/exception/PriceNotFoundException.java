package com.prgrms.ijuju.domain.stock.mid.exception;

public class PriceNotFoundException extends MidStockException{
    public PriceNotFoundException() {
        super(MidStockErrorCode.PRICE_NOT_FOUND);
    }
}
