package com.prgrms.ijuju.domain.notification.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FcmTokenRequest {
    @NotNull(message = "FCM 토큰은 필수입니다.")
    private String fcmToken;
}
