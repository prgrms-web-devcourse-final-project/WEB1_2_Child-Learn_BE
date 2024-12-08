package com.prgrms.ijuju.domain.minigame.flipcard.dto.response;

import lombok.Builder;

@Builder
public record PlayFlipCardAvailable(
        Boolean isBegin,
        Boolean isMid,
        Boolean isAdv
) {
    public static PlayFlipCardAvailable of(Boolean isBegin, Boolean isMid, Boolean isAdv) {
        return new PlayFlipCardAvailable(isBegin, isMid, isAdv);
    }
}
