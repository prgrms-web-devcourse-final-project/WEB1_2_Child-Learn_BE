package com.prgrms.ijuju.global.common.dto;

import com.prgrms.ijuju.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class CommonErrorResponse {
    private final String code;
    private final String message;
    private final int status;

    private CommonErrorResponse(final ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.status = errorCode.getHttpStatus().value();
    }

    public static CommonErrorResponse of(final ErrorCode errorCode) {
        return new CommonErrorResponse(errorCode);
    }
}
