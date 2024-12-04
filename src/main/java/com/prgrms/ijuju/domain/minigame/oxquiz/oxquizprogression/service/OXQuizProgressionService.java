package com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.service;

import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizdata.entity.OXQuizData;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.dto.response.QuizResponseDto;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.entity.OXQuizProgression;

import java.util.List;

public interface OXQuizProgressionService {

    List<OXQuizProgression> getProgressionsByMemberId(Long memberId);
    QuizResponseDto updateProgression(Long progressionId, String userAnswer);
    List<QuizResponseDto> getQuizzesForUser(Long memberId, String difficulty);
}