package com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.service;

import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizdata.entity.OXQuizData;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizdata.repository.OXQuizDataRepository;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.constant.Answer;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.dto.response.QuizAnswerResponseDto;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.dto.response.QuizResponseDto;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.entity.OXQuizProgression;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.exception.OXQuizInvalidAnswerException;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.exception.OXQuizNotFoundException;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.repository.OXQuizProgressionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OXQuizProgressionService  {

    private final OXQuizProgressionRepository oxQuizProgressionRepository;
    private final OXQuizDataRepository oxQuizDataRepository;

    public boolean checkDailyLimit(Long memberId) {
        Optional<OXQuizProgression> progression = oxQuizProgressionRepository.findByMemberId(memberId);

        if (progression.isPresent()) {
            LocalDate lastPlayedDate = progression.get().getLastPlayedDate();
            return !LocalDate.now().equals(lastPlayedDate); // 오늘 날짜와 비교
        }

        return true; // 진행 상태가 없으면 제한 없음
    }

    public void updateLastPlayedDate(Long memberId) {
        OXQuizProgression progression = oxQuizProgressionRepository.findByMemberId(memberId)
                .orElse(new OXQuizProgression());

        progression.setMemberId(memberId);
        progression.setLastPlayedDate(LocalDate.now());
        oxQuizProgressionRepository.save(progression);
    }


    public List<QuizResponseDto> getQuizzesForUser(Long memberId, String difficulty) {

        // 난이도에 맞는 문제를 필터링하여 가져옵니다.
        List<OXQuizData> quizzes = oxQuizDataRepository.findAll().stream()
                .filter(quiz -> quiz.getDifficulty().equalsIgnoreCase(difficulty))  // 난이도 필터링
                .collect(Collectors.toList());

        if (quizzes.isEmpty()) {
            throw new OXQuizNotFoundException();
        }

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
            throw new OXQuizInvalidAnswerException();
        }

        OXQuizData quiz = oxQuizDataRepository.findById(oxQuizDataId)
                .orElseThrow(OXQuizNotFoundException::new);

        Answer correctAnswer = Answer.fromString(quiz.getAnswer());
        boolean isCorrect = correctAnswer == Answer.fromString(userAnswer);


        return new QuizAnswerResponseDto(
                quiz.getId(),
                quiz.getExplanation(),
                isCorrect
        );
    }
}