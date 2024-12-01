package com.prgrms.ijuju.domain.stock.mid.dto.response;

import com.prgrms.ijuju.domain.stock.mid.exception.MidStockErrorCode;
import lombok.Getter;

@Getter
public class ErrorMidResponse {

    private final String message;
    private final int status;
    private final String code;

    public ErrorMidResponse(MidStockErrorCode errorCode) {
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus().value();
        this.code = errorCode.getCode();
    }

    public static ErrorMidResponse of (MidStockErrorCode errorCode) {
        return new ErrorMidResponse(errorCode);
    }

}
