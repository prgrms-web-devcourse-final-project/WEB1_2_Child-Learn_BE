package com.prgrms.ijuju.domain.notification.dto.response;

import com.prgrms.ijuju.domain.notification.entity.Notification;
import com.prgrms.ijuju.domain.notification.entity.NotificationType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SseNotificationResponseDto {
    private Long notificationId;
    private Long senderLoginId;
    private String senderUsername;
    private String title;
    private String content;
    private NotificationType type;
    private Boolean isRead;
    private LocalDateTime createdAt;
    private String profileImageUrl;

    public static SseNotificationResponseDto of(Notification notification, String profileImageUrl) {
        return SseNotificationResponseDto.builder()
                .notificationId(notification.getId())
                .senderLoginId(notification.getSenderLoginId())
                .senderUsername(notification.getSenderUsername())
                .title(notification.getTitle())
                .content(notification.getContent())
                .type(notification.getType())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .profileImageUrl(profileImageUrl)
                .build();
    }
}
