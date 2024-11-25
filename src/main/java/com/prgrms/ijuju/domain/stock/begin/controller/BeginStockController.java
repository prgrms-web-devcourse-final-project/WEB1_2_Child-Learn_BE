package com.prgrms.ijuju.domain.stock.begin.controller;

import com.prgrms.ijuju.domain.stock.begin.dto.response.BeginStockResponse;
import com.prgrms.ijuju.domain.stock.begin.service.BeginStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/begin-stocks")
@RestController
public class BeginStockController {
    private final BeginStockService beginStockService;

    @GetMapping("/")
    public ResponseEntity<BeginStockResponse> getBeginStock() {
        BeginStockResponse response = beginStockService.getBeginStockDataWithQuiz();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/submissions/{memberId}")
    public ResponseEntity<Void> createBeginQuizResult(@PathVariable("memberId") Long memberId) {
        beginStockService.updateBeginQuiz(memberId);
        return ResponseEntity.ok().build();
    }

}
