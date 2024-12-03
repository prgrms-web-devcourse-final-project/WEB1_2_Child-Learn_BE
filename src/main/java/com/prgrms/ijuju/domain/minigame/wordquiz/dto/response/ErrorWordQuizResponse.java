package com.prgrms.ijuju.domain.minigame.wordquiz.dto.response;

import com.prgrms.ijuju.domain.minigame.wordquiz.exception.WordQuizErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorWordQuizResponse {

    private final String message;
    private final HttpStatus status;
    private final String errorCode;

    private ErrorWordQuizResponse(WordQuizErrorCode errorCode) {
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus();
        this.errorCode = errorCode.getCode();
    }

    public static ErrorWordQuizResponse of(WordQuizErrorCode errorCode) {
        return new ErrorWordQuizResponse(errorCode);
    }
}
