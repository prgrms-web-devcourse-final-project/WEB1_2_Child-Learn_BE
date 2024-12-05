package com.prgrms.ijuju.domain.notification.controller;

import com.prgrms.ijuju.domain.notification.dto.request.FcmTokenRequest;
import com.prgrms.ijuju.domain.notification.service.FcmTokenService;
import com.prgrms.ijuju.global.auth.SecurityUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class FcmTokenController {
    private final FcmTokenService fcmTokenService;

    // FCM 토큰 등록
    @PostMapping("/token")
    public ResponseEntity<Void> registerToken(
            @AuthenticationPrincipal SecurityUser securityUser,
            @RequestBody FcmTokenRequest request
    ) {
        fcmTokenService.saveFCMToken(securityUser.getId(), request.getFcmToken());
        return ResponseEntity.ok().build();
    }

    // FCM 토큰 삭제
    @DeleteMapping("/token")
    public ResponseEntity<Void> deleteToken(
            @RequestBody @Valid FcmTokenRequest request
    ) {
        fcmTokenService.deleteToken(request.getFcmToken());
        return ResponseEntity.noContent().build();
    }
}
