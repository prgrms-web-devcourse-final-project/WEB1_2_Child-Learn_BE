package com.prgrms.ijuju.domain.member.dto.response;

import com.prgrms.ijuju.domain.member.exception.MemberErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorMemberResponse {

    private final String message;
    private final int status;
    private final String errorCode;

    public ErrorMemberResponse(MemberErrorCode memberErrorCode) {
        this.message = memberErrorCode.getMessage();
        this.status = memberErrorCode.getHttpStatus().value();
        this.errorCode = memberErrorCode.getCode();
    }

    public static ErrorMemberResponse of (MemberErrorCode errorCode) {
        return new ErrorMemberResponse(errorCode);
    }
}
