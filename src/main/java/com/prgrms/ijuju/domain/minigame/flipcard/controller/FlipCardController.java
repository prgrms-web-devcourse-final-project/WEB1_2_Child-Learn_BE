package com.prgrms.ijuju.domain.minigame.flipcard.controller;

import com.prgrms.ijuju.domain.minigame.flipcard.dto.response.FlipCardResponse;
import com.prgrms.ijuju.domain.minigame.flipcard.dto.response.PlayFlipCardAvailable;
import com.prgrms.ijuju.domain.minigame.flipcard.service.FlipCardService;
import com.prgrms.ijuju.global.auth.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/flip-card")
@Slf4j
public class FlipCardController {
    private final FlipCardService flipCardService;

    @GetMapping
    public ResponseEntity<List<FlipCardResponse>> showRandomCards(
            @RequestParam String difficulty) {
        log.info("랜덤 카드 조회: 난이도 {}", difficulty);

        return ResponseEntity.ok(flipCardService.findRandomCards(difficulty));
    }

    @GetMapping("/available")
    public ResponseEntity<PlayFlipCardAvailable> checkPlayAvailable(@AuthenticationPrincipal SecurityUser securityUser) {
        log.info("카드 뒤집기 게임 플레이 가능 여부 조회");
        Long memberId = securityUser.getId();
        PlayFlipCardAvailable playFlipCardAvailable = flipCardService.checkPlayAvailable(memberId);

        return ResponseEntity.ok(playFlipCardAvailable);
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<Void> updatePlayTime(
            @PathVariable Long memberId,
            @RequestParam String difficulty) {
        log.info("카드 뒤집기 게임 플레이 시간 갱신: memberId = {}, difficulty = {}", memberId, difficulty);
        flipCardService.saveOrUpdatePlay(memberId, difficulty);
        return ResponseEntity.noContent().build();
    }
}
