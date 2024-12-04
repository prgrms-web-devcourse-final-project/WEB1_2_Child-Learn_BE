package com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.service;

import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizdata.entity.OXQuizData;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizdata.repository.OXQuizDataRepository;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.dto.response.QuizAnswerResponseDto;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.dto.response.QuizResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OXQuizProgressionService  {

    private final OXQuizDataRepository oxQuizDataRepository;

    public List<QuizResponseDto> getQuizzesForUser(Long memberId, String difficulty) {
        // 난이도에 맞는 문제를 랜덤으로 3개 가져옵니다.
        List<OXQuizData> quizzes = oxQuizDataRepository.findAll().stream()
                .filter(quiz -> quiz.getDifficulty().equalsIgnoreCase(difficulty))
                .collect(Collectors.toList());

        // 랜덤으로 3개 문제를 섞어서 반환
        Collections.shuffle(quizzes);
        return quizzes.stream()
                .limit(3)
                .map(quiz -> new QuizResponseDto(
                        quiz.getId(),
                        quiz.getQuestion(),
                        quiz.getDifficulty()))
                .collect(Collectors.toList());
    }


    public QuizAnswerResponseDto checkAnswer(Long oxQuizDataId, String userAnswer) {
        // 퀴즈 데이터를 찾고 정답을 확인
        OXQuizData quiz = oxQuizDataRepository.findById(oxQuizDataId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));

        // 정답 여부 확인
        boolean isCorrect = quiz.getAnswer().equalsIgnoreCase(userAnswer);

        // 정답에 대한 설명을 반환
        return new QuizAnswerResponseDto(
                quiz.getId(),
                quiz.getExplanation(),
                isCorrect
        );
    }
}