package com.prgrms.ijuju.domain.stock.mid.exception;

public class MidStockException extends RuntimeException {
    private final MidStockErrorCode errorCode;

    public MidStockException(String message, MidStockErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public MidStockException(MidStockErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public MidStockErrorCode getErrorCode() {
        return errorCode;
    }


}
