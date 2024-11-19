package com.prgrms.ijuju.domain.example.controller;

import com.prgrms.ijuju.domain.example.service.SampleService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sample")
@RequiredArgsConstructor
@Slf4j
public class SampleController {
    private final SampleService sampleService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/list")
    public ResponseEntity<?> list(){
        log.info("--- list() ");
        return ResponseEntity.ok(
                new String[]{ "AAA", "BBB", "CCC" });
    }

    @GetMapping("/hello")
    public String hello(){
        sampleService.sampleMethod();
        return "Hello World!~";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/hellos")
    public String[] hellos(){
        return new String[]{ "Hello", "Hi" };
    }
}