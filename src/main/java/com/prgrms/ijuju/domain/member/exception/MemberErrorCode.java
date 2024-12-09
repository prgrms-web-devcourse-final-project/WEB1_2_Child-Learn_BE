package com.prgrms.ijuju.domain.member.exception;

import com.prgrms.ijuju.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MemberErrorCode implements ErrorCode {

    // 회원가입
    MEMBER_NOT_REGISTERED("CH001", "회원가입에 실패했습니다", HttpStatus.BAD_REQUEST),
    LOGINID_IS_DUPLICATED("CH002", "이미 존재하는 아이디입니다.", HttpStatus.BAD_REQUEST),
    EMAIL_IS_DUPLICATED("CH003","해당 이메일로 가입한 계정이 이미 존재합니다.", HttpStatus.BAD_REQUEST),
    USERNAME_IS_DUPLICATED("CH004", "이미 존재하는 닉네임입니다.", HttpStatus.BAD_REQUEST),

    // 로그인 및 로그아웃
    MEMBER_NOT_FOUND("CH005", "존재하지 않는 ID입니다.", HttpStatus.NOT_FOUND),
    MEMBER_LOGIN_DENIED("CH006", "로그인에 실패했습니다.", HttpStatus.BAD_REQUEST),
    MEMBER_ALREADY_LOGGED_IN("CH007","이미 다른 곳에서 로그인 중인 계정입니다.", HttpStatus.CONFLICT),
    MEMBER_ALREADY_LOGGED_OUT("CH008", "이미 로그아웃된 계정입니다.", HttpStatus.BAD_REQUEST),
    LOGIN_ID_REQUIRED("CH016", "로그인 아이디는 필수입니다.", HttpStatus.BAD_REQUEST),
    PASSWORD_REQUIRED("CH017", "비밀번호는 필수입니다.", HttpStatus.BAD_REQUEST),

    // 회원 수정
    MEMBER_NOT_REMOVED("CH009", "회원 탈퇴에 실패했습니다.", HttpStatus.BAD_REQUEST),
    MEMBER_NOT_MODIFIED("CH010", "회원 정보 수정에 실패했습니다.", HttpStatus.BAD_REQUEST),

    // 토큰
    MEMBER_ACCESSTOKEN_EXPIRED("CH011", "Access 토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    MEMBER_REFRESHTOKEN_EXPIRED("CH012", "Refresh 토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),

    // 회원 검색
    SEARCH_KEYWORD_EMPTY("CH013", "검색어를 입력해주세요.", HttpStatus.BAD_REQUEST),
    SEARCH_KEYWORD_TOO_SHORT("CH014", "검색어는 최소 2자 이상 입력해주세요.", HttpStatus.BAD_REQUEST),
    SEARCH_RESULT_NOT_FOUND("CH015", "검색 결과가 없습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    MemberErrorCode(String code, String message, HttpStatus httpStatus){
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

}
