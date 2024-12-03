package com.prgrms.ijuju.domain.notification.dto.request;

import com.prgrms.ijuju.domain.notification.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDto {
    private String title;
    private String content;
    private NotificationType type;
    private String fcmToken;
    private Long memberId;
}
