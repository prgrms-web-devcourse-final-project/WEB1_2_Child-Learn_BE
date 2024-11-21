package com.prgrms.ijuju.point.controller;

import com.prgrms.ijuju.point.dto.request.PointRequestDTO;
import com.prgrms.ijuju.point.dto.response.PointResponseDTO;
import com.prgrms.ijuju.point.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/points")
public class PointController {
    
    @Autowired
    private PointService pointService;

    // 포인트 업데이트
    @PostMapping("/update")
    public ResponseEntity<PointResponseDTO> updatePoints(@RequestBody PointRequestDTO request) {
        PointResponseDTO response = pointService.updatePoints(request);
        return ResponseEntity.ok(response);
    }

    // 포인트 잔액 조회
    @GetMapping("/balance/{memberId}")
    public ResponseEntity<PointResponseDTO> getPointBalance(@PathVariable Long memberId) {
        PointResponseDTO response = pointService.getPoint(memberId);
        return ResponseEntity.ok(response);
    }

    // 포인트 코인 교환
    @PostMapping("/exchange")
    public ResponseEntity<String> exchangePointsToCoins(@RequestParam Long memberId, @RequestParam Long pointsToExchange) {
        pointService.exchangePointsToCoins(memberId, pointsToExchange);
        return ResponseEntity.ok("Points successfully exchanged to coins.");
    }

}

