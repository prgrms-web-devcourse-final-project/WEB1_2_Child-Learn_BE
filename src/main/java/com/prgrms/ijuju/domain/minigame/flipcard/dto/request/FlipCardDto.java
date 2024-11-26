package com.prgrms.ijuju.domain.minigame.flipcard.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prgrms.ijuju.domain.minigame.flipcard.entity.FlipCard;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FlipCardDto {
    private String word;
    private String description;
    private String category;

    @JsonProperty("Word")
    public void setWord(String word) {
        this.word = word;
    }

    @JsonProperty("Description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("Category")
    public void setCategory(String category) {
        this.category = category;
    }

    public FlipCard toEntity() {
        return FlipCard.builder()
                .cardTitle(this.word)
                .cardContent(this.description)
                .cardCategory(this.category)
                .build();
    }
}
