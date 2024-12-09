package com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.controller;

import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.dto.request.QuizAnswerRequestDto;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.dto.request.QuizRequestDto;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.dto.response.QuizAnswerResponseDto;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.dto.response.QuizResponseDto;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.service.OXQuizProgressionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ox-quiz-progression")
@RequiredArgsConstructor
public class OXQuizProgressionController {

    private final OXQuizProgressionService progressionService;

    // 퀴즈 시작 API
    @PostMapping("/start")
    public ResponseEntity<List<QuizResponseDto>> getQuizzesForUser(@RequestBody QuizRequestDto requestDTO) {
        Long memberId = requestDTO.getMemberId();
        String difficulty = requestDTO.getDifficulty();

        if (!progressionService.checkDailyLimit(memberId, difficulty)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Collections.singletonList(
                            new QuizResponseDto(null, "해당 난이도는 하루에 한 번만 플레이할 수 있습니다.", null)
                    ));
        }
        
        List<QuizResponseDto> quizzes = progressionService.getQuizzesForUser(requestDTO.getMemberId(), requestDTO.getDifficulty());
        progressionService.updateLastPlayedDate(memberId, difficulty);

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
