package com.prgrms.ijuju.domain.minigame.flipcard.controller;

import com.prgrms.ijuju.domain.minigame.flipcard.dto.response.FlipCardResponse;
import com.prgrms.ijuju.domain.minigame.flipcard.dto.response.PlayFlipCardAvailable;
import com.prgrms.ijuju.domain.minigame.flipcard.service.FlipCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/flip-card")
public class FlipCardController {
    private final FlipCardService flipCardService;

    @GetMapping
    public ResponseEntity<List<FlipCardResponse>> showRandomCards(
            @RequestParam String difficulty) {

        return ResponseEntity.ok(flipCardService.findRandomCards(difficulty));
    }

    @GetMapping("/available")
    public ResponseEntity<PlayFlipCardAvailable> checkPlayAvailable() {
        Long memberId = 1L; // 임시로 memberId를 1로 설정
        PlayFlipCardAvailable playFlipCardAvailable = flipCardService.checkPlayAvailable(memberId);

        return ResponseEntity.ok(playFlipCardAvailable);
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<Void> updatePlayTime(
            @PathVariable Long memberId,
            @RequestParam String difficulty) {

        flipCardService.saveOrUpdatePlay(memberId, difficulty);
        return ResponseEntity.noContent().build();
    }
}
