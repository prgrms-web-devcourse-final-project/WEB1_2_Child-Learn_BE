package com.prgrms.ijuju.domain.minigame.wordquiz.dto.response;

public record WordQuizAvailabilityResponse(
        boolean isEasyPlayAvailable,
        boolean isNormalPlayAvailable,
        boolean isHardPlayAvailable
) {}
