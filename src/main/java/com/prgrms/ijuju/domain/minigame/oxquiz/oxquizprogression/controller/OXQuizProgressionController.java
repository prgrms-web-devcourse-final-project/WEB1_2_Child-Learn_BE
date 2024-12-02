package com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.controller;

import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.dto.request.QuizRequestDto;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.dto.response.QuizResponseDto;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.entity.OXQuizProgression;
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

    @GetMapping("/{memberId}")
    public ResponseEntity<List<OXQuizProgression>> getProgressions(@PathVariable Long memberId) {
        List<OXQuizProgression> progressions = progressionService.getProgressionsByMemberId(memberId);
        return ResponseEntity.ok(progressions);
    }

    @PostMapping("/{progressionId}")
    public ResponseEntity<QuizResponseDto> updateProgression(
            @PathVariable Long progressionId,
            @RequestBody String userAnswer) {

        // 서비스 로직 호출
        QuizResponseDto responseDto = progressionService.updateProgression(progressionId, userAnswer);

        // 응답 반환
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/start")
    public ResponseEntity<List<QuizResponseDto>> getQuizzesForUser(@RequestBody QuizRequestDto requestDTO) {
        // 기존의 getQuizzesForUser 메서드를 호출하여 퀴즈 데이터를 가져옴
        List<QuizResponseDto> quizzes = progressionService.getQuizzesForUser(requestDTO.getMemberId(), requestDTO.getDifficulty());
        return ResponseEntity.ok(quizzes);
    }
}