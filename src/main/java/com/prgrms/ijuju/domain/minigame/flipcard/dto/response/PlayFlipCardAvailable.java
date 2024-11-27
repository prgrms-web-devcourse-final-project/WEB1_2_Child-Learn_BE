package com.prgrms.ijuju.domain.minigame.flipcard.dto.response;

import lombok.Builder;

@Builder
public record PlayFlipCardAvailable(
        boolean isBegin,
        boolean isMid,
        boolean isAdv
) {
    public static PlayFlipCardAvailable of(boolean isBegin, boolean isMid, boolean isAdv) {
        return new PlayFlipCardAvailable(isBegin, isMid, isAdv);
    }
}
