package com.prgrms.ijuju.domain.minigame.wordquiz.dto.response;

import com.prgrms.ijuju.domain.minigame.wordquiz.entity.Difficulty;
import com.prgrms.ijuju.domain.minigame.wordquiz.entity.WordQuiz;

public record WordQuizResponse(
        String word,
        String explanation,
        String hint,
        int currentPhase,
        int remainLife,
        Difficulty difficulty,
        boolean isGameOver
) {
    public WordQuizResponse withUpdatedPhase(int newPhase) {
        return new WordQuizResponse(word, explanation, hint, newPhase, remainLife, difficulty, isGameOver);
    }

    public WordQuizResponse withUpdatedLife(int newLife) {
        boolean gameOver = newLife <= 0;
        return new WordQuizResponse(word, explanation, hint, currentPhase, newLife, difficulty, gameOver);
    }

    public WordQuizResponse withGameOver(boolean gameOver) {
        return new WordQuizResponse(word, explanation, hint, currentPhase, remainLife, difficulty, gameOver);
    }

    public WordQuizResponse withNewQuiz(WordQuiz quiz) {
        return new WordQuizResponse(
                quiz.getWord(),
                quiz.getExplanation(),
                quiz.getHint(),
                this.currentPhase,
                this.remainLife,
                this.difficulty,
                false
        );
    }
}
