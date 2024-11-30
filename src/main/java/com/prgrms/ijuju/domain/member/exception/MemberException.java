package com.prgrms.ijuju.domain.member.exception;

import org.springframework.http.HttpStatus;

public enum MemberException {

    MEMBER_NOT_FOUND("존재하지 않는 ID입니다.", HttpStatus.NOT_FOUND),
    MEMBER_LOGIN_DENIED("로그인에 실패했습니다.", HttpStatus.BAD_REQUEST),
    MEMBER_NOT_REGISTERED("회원가입에 실패했습니다", HttpStatus.BAD_REQUEST),
    MEMBER_NOT_REMOVED("회원 탈퇴에 실패했습니다.", HttpStatus.BAD_REQUEST),
    MEMBER_NOT_MODIFIED("회원 정보 수정에 실패했습니다.", HttpStatus.BAD_REQUEST),
    LOGINID_IS_DUPLICATED("이미 존재하는 아이디입니다.", HttpStatus.BAD_REQUEST),
    MEMBER_ACCESSTOKEN_EXPIRED("Access 토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    MEMBER_REFRESHTOKEN_EXPIRED("Refresh 토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    SEARCH_KEYWORD_EMPTY("검색어를 입력해주세요.", HttpStatus.BAD_REQUEST),
    SEARCH_KEYWORD_TOO_SHORT("검색어는 최소 2자 이상 입력해주세요.", HttpStatus.BAD_REQUEST),
    SEARCH_RESULT_NOT_FOUND("검색 결과가 없습니다.", HttpStatus.NOT_FOUND);

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
