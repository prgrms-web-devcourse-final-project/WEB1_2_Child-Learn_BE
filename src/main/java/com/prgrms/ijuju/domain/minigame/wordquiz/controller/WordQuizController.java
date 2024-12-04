package com.prgrms.ijuju.domain.minigame.wordquiz.controller;

import com.prgrms.ijuju.domain.minigame.wordquiz.dto.request.WordQuizRequest;
import com.prgrms.ijuju.domain.minigame.wordquiz.dto.response.WordQuizAvailabilityResponse;
import com.prgrms.ijuju.domain.minigame.wordquiz.dto.response.WordQuizResponse;
import com.prgrms.ijuju.domain.minigame.wordquiz.entity.Difficulty;
import com.prgrms.ijuju.domain.minigame.wordquiz.service.WordQuizService;
import com.prgrms.ijuju.global.auth.SecurityUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/word-quiz")
@RestController
public class WordQuizController {

    private final WordQuizService wordQuizService;

    @GetMapping("/availability")
    public ResponseEntity<WordQuizAvailabilityResponse> checkAvailability(@AuthenticationPrincipal SecurityUser user) {
        log.info("낱말 게임 플레이 가능 여부 조회");
        return ResponseEntity.ok(wordQuizService.checkPlayAvailability(user.getId()));
    }

    @GetMapping("/{difficulty}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<WordQuizResponse> startWordQuiz(@AuthenticationPrincipal SecurityUser user,
                                                          @PathVariable Difficulty difficulty,
                                                          HttpSession session) {
        log.info("낱말 게임 시작 요청: 닉네임 = {}, 난이도 = {}", user.getUsername(), difficulty);
        WordQuizResponse wordQuiz = wordQuizService.startOrContinueWordQuiz(session, user.getId(), difficulty);

        return ResponseEntity.ok(wordQuiz);
    }

    @PostMapping("/submissions")
    public ResponseEntity<WordQuizResponse> handleAnswer(@AuthenticationPrincipal SecurityUser user,
                                                         @RequestBody WordQuizRequest wordQuizRequest,
                                                         HttpSession session) {
        WordQuizResponse gameState = wordQuizService.handleAnswer(user.getId(), session, wordQuizRequest.isCorrect());

        if (wordQuizRequest.isCorrect()) {
            gameState = wordQuizService.startOrContinueWordQuiz(session, user.getId(), gameState.difficulty());
        }

        return ResponseEntity.ok(gameState);
    }
}
