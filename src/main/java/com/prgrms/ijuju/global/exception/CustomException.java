package com.prgrms.ijuju.global.exception;

import com.prgrms.ijuju.domain.chat.exception.ChatException;
import com.prgrms.ijuju.domain.friend.exception.FriendException;
import com.prgrms.ijuju.domain.member.exception.MemberException;
import com.prgrms.ijuju.domain.wallet.exception.WalletException;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final String code;
    private final int status;

    public CustomException(FriendException exception) {
        super(exception.getMessage());
        this.code = exception.getCode();
        this.status = exception.getHttpStatus().value();
    }

    public CustomException(WalletException exception) {
        super(exception.getMessage());
        this.code = exception.getCode();
        this.status = exception.getHttpStatus().value();
    }

    public CustomException(ChatException exception) {
        super(exception.getMessage());
        this.code = exception.getCode();
        this.status = exception.getHttpStatus().value();
    }
}