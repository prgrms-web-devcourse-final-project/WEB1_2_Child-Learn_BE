package com.prgrms.ijuju.domain.minigame.flipcard.dto.response;

import com.prgrms.ijuju.domain.minigame.flipcard.entity.FlipCard;
import lombok.Builder;

@Builder
public record FlipCardResponse(
        Long id,
        String cardTitle,
        String cardContent,
        String cardCategory
) {
    public static FlipCardResponse of(FlipCard flipCard) {
        return new FlipCardResponse(
                flipCard.getId(),
                flipCard.getCardTitle(),
                flipCard.getCardContent(),
                flipCard.getCardCategory()
        );
    }
}
