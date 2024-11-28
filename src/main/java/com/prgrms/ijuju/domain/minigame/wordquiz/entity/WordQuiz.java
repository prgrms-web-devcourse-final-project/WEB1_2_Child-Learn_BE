package com.prgrms.ijuju.domain.minigame.wordquiz.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WordQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String word;

    @NotNull
    private String explanation;

    @NotNull
    private String hint;

    @Builder
    public WordQuiz(String word, String explanation, String hint) {
        this.word = word;
        this.explanation = explanation;
        this.hint = hint;
    }
}
