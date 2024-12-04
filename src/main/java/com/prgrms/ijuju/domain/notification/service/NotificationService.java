package com.prgrms.ijuju.domain.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.prgrms.ijuju.domain.notification.dto.response.NotificationResponseDto;
import com.prgrms.ijuju.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final FirebaseMessaging firebaseMessaging;

//    public Slice<NotificationResponseDto> getNotifications(Long memberId, Pageable pageable) {
//        return notificationRepository.findReceiverNotifications(memberId, pageable)
//                .map(NotificationResponseDto::of);
//    }
}
