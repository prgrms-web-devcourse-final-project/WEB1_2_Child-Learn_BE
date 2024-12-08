package com.prgrms.ijuju.domain.infra.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @GetMapping("/api/v1/healthcheck")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }
}
