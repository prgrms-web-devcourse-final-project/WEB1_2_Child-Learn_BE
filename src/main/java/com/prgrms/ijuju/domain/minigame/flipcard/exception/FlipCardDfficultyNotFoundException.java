package com.prgrms.ijuju.domain.minigame.flipcard.exception;

public class FlipCardDfficultyNotFoundException extends FlipCardException {
    public FlipCardDfficultyNotFoundException() {
        super(FlipCardErrorCode.DIFFICULTY_NOT_FOUND);
    }
}
