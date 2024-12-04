package com.prgrms.ijuju.domain.minigame.wordquiz.dto.response;

import com.prgrms.ijuju.domain.minigame.wordquiz.entity.Difficulty;

public record WordQuizResponse(
        String word,
        String explanation,
        String hint,
        int currentPhase,
        int remainLife,
        Difficulty difficulty
) {
    public WordQuizResponse withUpdatedPhase(int newPhase) {
        return new WordQuizResponse(word, explanation, hint, newPhase, remainLife, difficulty);
    }

    public WordQuizResponse withUpdatedLife(int newLife) {
        return new WordQuizResponse(word, explanation, hint, currentPhase, newLife, difficulty);
    }
}
