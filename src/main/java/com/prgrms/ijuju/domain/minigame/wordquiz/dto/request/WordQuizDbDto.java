package com.prgrms.ijuju.domain.minigame.wordquiz.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prgrms.ijuju.domain.minigame.wordquiz.entity.WordQuiz;
import lombok.Getter;

@Getter
public class WordQuizDbDto {
    private String word;
    private String explanation;
    private String hint;

    @JsonProperty("Word")
    public void setWord(String word) {
        this.word = word;
    }

    @JsonProperty("Description")
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    @JsonProperty("Hint")
    public void setHint(String hint) {
        this.hint = hint;
    }

    public WordQuiz toEntity() {
        return WordQuiz.builder()
                .word(this.word)
                .explanation(this.explanation)
                .hint(this.hint)
                .build();
    }
}
