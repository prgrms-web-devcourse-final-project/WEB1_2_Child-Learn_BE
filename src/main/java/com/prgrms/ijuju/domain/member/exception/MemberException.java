package com.prgrms.ijuju.domain.member.exception;

import com.prgrms.ijuju.global.exception.BusinessException;

public class MemberException extends BusinessException {

    public MemberException(MemberErrorCode errorCode) {
        super(errorCode);
    }
}
