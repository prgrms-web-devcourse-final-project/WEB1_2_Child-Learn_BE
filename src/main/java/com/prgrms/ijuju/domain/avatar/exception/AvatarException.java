package com.prgrms.ijuju.domain.avatar.exception;

import org.springframework.http.HttpStatus;

public enum AvatarException {

    AVATAR_NOT_FOUND("아바타가 존재하지 않습니다.", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus status;

    AvatarException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public ItemTaskException getItemTaskException(){
        return new ItemTaskException(this.message, this.status.value());
    }
}
