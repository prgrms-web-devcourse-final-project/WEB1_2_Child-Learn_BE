package com.prgrms.ijuju.domain.stock.begin.controller;

import com.prgrms.ijuju.domain.stock.begin.dto.response.BeginStockResponse;
import com.prgrms.ijuju.domain.stock.begin.service.BeginStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BeginStockController {
    private final BeginStockService beginStockService;

    @GetMapping("/api/v1/begin-stocks")
    public ResponseEntity<BeginStockResponse> getBeginStock() {
        BeginStockResponse response = beginStockService.getBeginStockDataWithQuiz();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/v1/begin-stocks/submissions/{memberId}")
    public ResponseEntity<Void> createBeginQuizResult(@PathVariable("memberId") Long memberId) {
        beginStockService.updateBeginQuiz(memberId);
        return ResponseEntity.ok().build();
    }

}
