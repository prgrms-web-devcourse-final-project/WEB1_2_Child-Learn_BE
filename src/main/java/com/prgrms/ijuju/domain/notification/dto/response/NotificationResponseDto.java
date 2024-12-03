package com.prgrms.ijuju.domain.notification.dto.response;

import com.prgrms.ijuju.domain.notification.entity.Notification;
import com.prgrms.ijuju.domain.notification.entity.NotificationType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResponseDto {
    private Long id;
    private String title;
    private String content;
    private NotificationType type;
    private Boolean isRead;
    private LocalDateTime createdAt;

    public static NotificationResponseDto from(Notification notification) {
        return NotificationResponseDto.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .type(notification.getType())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
