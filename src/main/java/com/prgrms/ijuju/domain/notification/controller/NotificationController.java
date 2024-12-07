package com.prgrms.ijuju.domain.notification.controller;

import com.prgrms.ijuju.domain.notification.dto.response.NotificationResponseDto;
import com.prgrms.ijuju.domain.notification.service.NotificationService;
import com.prgrms.ijuju.global.auth.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {
    private final NotificationService notificationService;

    // 알림 목록 조회
    @GetMapping
    public ResponseEntity<Slice<NotificationResponseDto>> getNotifications(
            @AuthenticationPrincipal UserDetails userDetails,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(
                notificationService.getNotifications(userDetails.getUsername(), pageable)
        );
    }

    // 알림 읽음 처리
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<String> markAsRead(
            @PathVariable Long notificationId,
            @AuthenticationPrincipal SecurityUser securityUser
    ) {
        log.info(("알림 읽음 처리"));
        notificationService.markAsRead(notificationId, securityUser.getId());
        return ResponseEntity.ok("알림 읽음 처리 완료.");
    }

    // 모든 알림 읽음 처리
    @PatchMapping("/all/read")
    public ResponseEntity<String> markAllAsRead(
            @AuthenticationPrincipal SecurityUser securityUser
    ) {
        log.info("모든 알림 읽음 처리");
        notificationService.markAllAsRead(securityUser.getId());
        return ResponseEntity.ok("모든 알림 읽음 처리 완료.");
    }

    // 알림 삭제 처리
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<String> deleteNotification(
            @PathVariable Long notificationId,
            @AuthenticationPrincipal SecurityUser securityUser
    ) {
        log.info("알림 삭제 처리");
        notificationService.markAsDeleted(notificationId, securityUser.getId());
        return ResponseEntity.ok("알림 삭제 완료");
    }
}
