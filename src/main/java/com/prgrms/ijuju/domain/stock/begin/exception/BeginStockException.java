package com.prgrms.ijuju.domain.stock.begin.exception;

public class BeginStockException extends RuntimeException {

    public BeginStockException(BeginStockErrorCode beginStockErrorCode) {
        super(beginStockErrorCode.getMessage());
    }
}
