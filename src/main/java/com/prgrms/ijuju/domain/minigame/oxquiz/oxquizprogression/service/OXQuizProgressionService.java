package com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.service;

import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizdata.entity.OXQuizData;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizdata.repository.OXQuizDataRepository;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.constant.Answer;
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

        // 난이도에 맞는 문제를 필터링하여 가져옵니다.
        List<OXQuizData> quizzes = oxQuizDataRepository.findAll().stream()
                .filter(quiz -> quiz.getDifficulty().equalsIgnoreCase(difficulty))  // 난이도 필터링
                .collect(Collectors.toList());

        // 문제를 랜덤으로 섞기
        Collections.shuffle(quizzes);

        // 3개의 문제를 선택하여 QuizResponseDto로 변환 후 반환
        return quizzes.stream()
                .limit(3)
                .map(quiz -> new QuizResponseDto(
                        quiz.getId(),
                        quiz.getQuestion(),
                        quiz.getDifficulty()
                       )
                )
                .collect(Collectors.toList());
    }


    public QuizAnswerResponseDto checkAnswer(Long oxQuizDataId, String userAnswer) {
        if (!userAnswer.equalsIgnoreCase("O") && !userAnswer.equalsIgnoreCase("X")) {
            throw new IllegalArgumentException("O 또는 X 만이 가능합니다 " + userAnswer);
        }

        OXQuizData quiz = oxQuizDataRepository.findById(oxQuizDataId)
                .orElseThrow(() -> new IllegalArgumentException("해당 퀴즈의 Id 가 틀렸습니다"));

        Answer correctAnswer = Answer.fromString(quiz.getAnswer());
        boolean isCorrect = correctAnswer == Answer.fromString(userAnswer);


        return new QuizAnswerResponseDto(
                quiz.getId(),
                quiz.getExplanation(),
                isCorrect
        );
    }
}