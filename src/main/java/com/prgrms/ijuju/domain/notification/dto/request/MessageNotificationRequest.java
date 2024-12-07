package com.prgrms.ijuju.domain.notification.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageNotificationRequest {
    private String receiverLoginId;
    private String messageContent;
}
