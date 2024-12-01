package com.prgrms.ijuju.domain.stock.mid.exception;

public class MidPriceNotFoundException extends MidStockException{
    public MidPriceNotFoundException() {
        super(MidStockErrorCode.PRICE_NOT_FOUND);
    }
}
