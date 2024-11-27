package com.prgrms.ijuju.domain.stock.begin.controller;

import com.prgrms.ijuju.domain.stock.begin.dto.response.BeginStockResponse;
import com.prgrms.ijuju.domain.stock.begin.service.BeginStockService;
import com.prgrms.ijuju.global.auth.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/begin-stocks")
@RestController
public class BeginStockController {
    private final BeginStockService beginStockService;

    @GetMapping
    public ResponseEntity<BeginStockResponse> getBeginStock() {
        log.info("주식 데이터 요청 컨트롤러 실행");
        BeginStockResponse response = beginStockService.getBeginStockDataWithQuiz();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/submissions")
    public ResponseEntity<Void> createBeginQuizResult(@AuthenticationPrincipal SecurityUser user) {
        beginStockService.playBeginStockQuiz(user.getId());
        return ResponseEntity.ok().build();
    }


}
