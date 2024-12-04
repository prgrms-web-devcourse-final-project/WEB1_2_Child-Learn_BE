package com.prgrms.ijuju.domain.stock.begin.controller;

import com.prgrms.ijuju.domain.stock.begin.dto.response.BeginStockResponse;
import com.prgrms.ijuju.domain.stock.begin.service.BeginStockGptService;
import com.prgrms.ijuju.domain.stock.begin.service.BeginStockPriceService;
import com.prgrms.ijuju.domain.stock.begin.service.BeginStockService;
import com.prgrms.ijuju.global.auth.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/begin-stocks")
@RestController
public class BeginStockController {
    private final BeginStockService beginStockService;
    private final BeginStockPriceService beginStockPriceService;
    private final BeginStockGptService beginStockGptService;

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

    @Profile("dev")
    @PostMapping("/generations/manual")
    public ResponseEntity<String> generateDailyData() {
        log.info("오늘의 모의투자 초급 데이터 수동 생성");
        try {
            beginStockPriceService.createWeeklyStockPrice();

            for (int i = 0; i < 5; i++) {
                beginStockGptService.generateBeginQuiz();
            }
            return ResponseEntity.ok("Daily data generated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to generate data: " + e.getMessage());
        }
    }

}
