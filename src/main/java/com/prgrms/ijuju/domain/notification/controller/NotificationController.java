package com.prgrms.ijuju.domain.notification.controller;

import com.prgrms.ijuju.domain.notification.dto.request.FriendNotificationRequest;
import com.prgrms.ijuju.domain.notification.dto.request.MessageNotificationRequest;
import com.prgrms.ijuju.domain.notification.dto.response.NotificationResponseDto;
import com.prgrms.ijuju.domain.notification.service.NotificationService;
import com.prgrms.ijuju.global.auth.SecurityUser;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long notificationId,
            @AuthenticationPrincipal SecurityUser securityUser
    ) {
        notificationService.markAsRead(notificationId, securityUser.getId());
        return ResponseEntity.ok().build();
    }

    // 모든 알림 읽음 처리
    @PatchMapping("/all/read")
    public ResponseEntity<Void> markAllAsRead(
            @AuthenticationPrincipal SecurityUser securityUser
    ) {
        notificationService.markAllAsRead(securityUser.getId());
        return ResponseEntity.ok().build();
    }

    // 알림 삭제 처리
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable Long notificationId,
            @AuthenticationPrincipal SecurityUser securityUser
    ) {
        notificationService.markAsDeleted(notificationId, securityUser.getId());
        return ResponseEntity.noContent().build();
    }

    // 친구 요청 알림 생성
    @PostMapping("/friend-request")
    public ResponseEntity<Void> createFriendRequestNotification(
            @AuthenticationPrincipal SecurityUser securityUser,
            @RequestBody FriendNotificationRequest request
    ) {
        notificationService.createFriendRequestNotification(
                securityUser.getUsername(),
                request.getReceiverLoginId()
        );
        return ResponseEntity.ok().build();
    }

    // 친구 수락 알림 생성
    @PostMapping("/friend-accept")
    public ResponseEntity<Void> createFriendAcceptNotification(
            @AuthenticationPrincipal SecurityUser securityUser,
            @RequestBody FriendNotificationRequest request
    ) {
        notificationService.createFriendAcceptNotification(
                securityUser.getUsername(),
                request.getReceiverLoginId()
        );
        return ResponseEntity.ok().build();
    }

    // 메시지 알림 생성
    @PostMapping("/message")
    public ResponseEntity<Void> createMessageNotification(
            @AuthenticationPrincipal SecurityUser securityUser,
            @RequestBody MessageNotificationRequest request
    ) {
        notificationService.createMessageNotification(
                securityUser.getUsername(),
                request.getReceiverLoginId(),
                request.getMessageContent()
        );
        return ResponseEntity.ok().build();
    }
}
