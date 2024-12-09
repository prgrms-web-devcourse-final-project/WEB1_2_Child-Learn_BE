package com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.exception;

import com.prgrms.ijuju.global.exception.BusinessException;

public class OXQuizNotFoundException extends BusinessException {
    public OXQuizNotFoundException() {
        super(OXQuizErrorCode.QUIZ_NOT_FOUND);
    }
}