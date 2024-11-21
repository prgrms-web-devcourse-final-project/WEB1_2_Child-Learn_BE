package com.prgrms.ijuju.domain.member.exception;

import org.springframework.http.HttpStatus;

public enum MemberException {

    MEMBER_NOT_FOUND("존재하지 않는 ID입니다.", HttpStatus.NOT_FOUND),
    MEMBER_NOT_REGISTERED("회원가입에 실패했습니다", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus status;

    MemberException(String message, HttpStatus status){
        this.message = message;
        this.status = status;
    }

    public MemberTaskException getMemberTaskException(){
        return new MemberTaskException(this.message, this.status.value());
    }



}
