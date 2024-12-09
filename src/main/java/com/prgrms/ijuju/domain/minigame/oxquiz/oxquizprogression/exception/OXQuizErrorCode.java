package com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.exception;

import com.prgrms.ijuju.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum OXQuizErrorCode implements ErrorCode {
    QUIZ_NOT_FOUND("OXQUIZ_001", "해당 퀴즈를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_ANSWER("OXQUIZ_002", "정답은 O 또는 X만 입력 가능합니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    OXQuizErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}