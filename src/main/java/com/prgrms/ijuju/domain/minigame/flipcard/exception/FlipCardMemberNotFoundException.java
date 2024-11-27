package com.prgrms.ijuju.domain.minigame.flipcard.exception;

public class FlipCardMemberNotFoundException extends FlipCardException {
    public FlipCardMemberNotFoundException() {
        super(FlipCardErrorCode.MEMBER_NOT_FOUND);
    }
}