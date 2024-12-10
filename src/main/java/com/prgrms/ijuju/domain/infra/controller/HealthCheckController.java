package com.prgrms.ijuju.domain.infra.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
public class HealthCheckController {
    @GetMapping("/api/v1/healthcheck")
    public ResponseEntity<String> healthCheck() {
        log.info("Health check requested at {}", LocalDateTime.now());
        try {
            return ResponseEntity.ok("OK");
        } finally {
            log.info("Health check responded at {}", LocalDateTime.now());
        }
    }
}
