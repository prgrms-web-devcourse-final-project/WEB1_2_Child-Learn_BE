package com.prgrms.ijuju.domain.avatar.dto.response;

import com.prgrms.ijuju.domain.avatar.exception.ItemErrorCode;
import lombok.Getter;

@Getter
public class ErrorItemResponse {

    private final String message;
    private final int status;
    private final String errorCode;

    public ErrorItemResponse(ItemErrorCode itemErrorCode) {
        this.message = itemErrorCode.getMessage();
        this.status = itemErrorCode.getHttpStatus().value();
        this.errorCode = itemErrorCode.getCode();
    }

    public static ErrorItemResponse of (ItemErrorCode errorCode) {
        return new ErrorItemResponse(errorCode);
    }
}
