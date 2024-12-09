package com.prgrms.ijuju.domain.minigame.wordquiz.exception;

import com.prgrms.ijuju.global.exception.BusinessException;

public class WordQuizException extends BusinessException {

    public WordQuizException(WordQuizErrorCode errorCode) {
        super(errorCode);
    }
}
