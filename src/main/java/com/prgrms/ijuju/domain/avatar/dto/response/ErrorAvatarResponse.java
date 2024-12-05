package com.prgrms.ijuju.domain.avatar.dto.response;

import com.prgrms.ijuju.domain.avatar.exception.AvatarErrorCode;
import lombok.Getter;

@Getter
public class ErrorAvatarResponse {

    private final String message;
    private final int status;
    private final String errorCode;

    public ErrorAvatarResponse(AvatarErrorCode avatarErrorCode) {
        this.message = avatarErrorCode.getMessage();
        this.status = avatarErrorCode.getHttpStatus().value();
        this.errorCode = avatarErrorCode.getCode();
    }

    public static ErrorAvatarResponse of (AvatarErrorCode errorCode) {
        return new ErrorAvatarResponse(errorCode);
    }
}
