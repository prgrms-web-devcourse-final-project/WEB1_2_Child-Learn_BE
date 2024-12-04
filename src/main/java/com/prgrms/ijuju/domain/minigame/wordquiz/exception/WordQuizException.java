package com.prgrms.ijuju.domain.minigame.wordquiz.exception;

public class WordQuizException extends RuntimeException {
    private final WordQuizErrorCode errorCode;

    public WordQuizException(WordQuizErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public WordQuizErrorCode getErrorCode(){
        return errorCode;
    }
}
