package com.prgrms.ijuju.domain.notification.dto.response;

import com.prgrms.ijuju.domain.notification.entity.Notification;
import com.prgrms.ijuju.domain.notification.entity.NotificationType;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResponseDto {
    private Long id;
    private Long senderLoginId;
    private String senderUsername;
    private String title;
    private String content;
    private NotificationType type;
    private Boolean isRead;
    private LocalDateTime createdAt;
    private String profileImageUrl;
    private String elapsedTime;    // "20분 전" 같은 경과 시간

    public static NotificationResponseDto of(Notification notification, String profileImageUrl) {
        return NotificationResponseDto.builder()
                .id(notification.getId())
                .senderLoginId(notification.getSenderLoginId())
                .senderUsername(notification.getSenderUsername())
                .title(notification.getTitle())
                .content(notification.getContent())
                .type(notification.getType())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .profileImageUrl(profileImageUrl)
                .elapsedTime(calculateElapsedTime(notification.getCreatedAt()))
                .build();
    }

    private static String calculateElapsedTime(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, now);
        long seconds = duration.getSeconds();

        if (seconds < 60) {
            return "방금 전";
        }

        long minutes = seconds / 60;
        if (minutes < 60) {
            return minutes + "분 전";
        }

        long hours = minutes / 60;
        if (hours < 24) {
            return hours + "시간 전";
        }

        long days = hours / 24;
        if (days < 30) {
            return days + "일 전";
        }

        long months = days / 30;
        if (months < 12) {
            return months + "개월 전";
        }

        long years = months / 12;
        return years + "년 전";
    }
}
