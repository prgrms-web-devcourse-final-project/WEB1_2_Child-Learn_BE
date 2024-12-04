package com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.controller;

import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.dto.request.QuizAnswerRequestDto;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.dto.request.QuizRequestDto;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.dto.response.QuizAnswerResponseDto;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.dto.response.QuizResponseDto;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.service.OXQuizProgressionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ox-quiz-progression")
@RequiredArgsConstructor
public class OXQuizProgressionController {

    private final OXQuizProgressionService progressionService;

    // 퀴즈 시작 API
    @PostMapping("/start")
    public ResponseEntity<List<QuizResponseDto>> getQuizzesForUser(@RequestBody QuizRequestDto requestDTO) {
        List<QuizResponseDto> quizzes = progressionService.getQuizzesForUser(requestDTO.getMemberId(), requestDTO.getDifficulty());
        return ResponseEntity.ok(quizzes);
    }

    // 답안 제출 API
    @PostMapping("/{oxQuizDataId}")
    public ResponseEntity<QuizAnswerResponseDto> submitAnswer(
            @PathVariable Long oxQuizDataId,
            @RequestBody QuizAnswerRequestDto requestDto) {
        String userAnswer = requestDto.getUserAnswer();
        QuizAnswerResponseDto response = progressionService.checkAnswer(oxQuizDataId, userAnswer);
        return ResponseEntity.ok(response);
    }
}
