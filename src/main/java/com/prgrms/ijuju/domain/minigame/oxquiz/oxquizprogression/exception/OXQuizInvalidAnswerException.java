package com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.exception;

import com.prgrms.ijuju.global.exception.BusinessException;

public class OXQuizInvalidAnswerException extends BusinessException {
    public OXQuizInvalidAnswerException() {
        super(OXQuizErrorCode.INVALID_ANSWER);
    }
}