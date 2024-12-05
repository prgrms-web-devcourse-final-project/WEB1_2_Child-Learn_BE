package com.prgrms.ijuju.domain.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.notification.dto.response.NotificationResponseDto;
import com.prgrms.ijuju.domain.notification.entity.Notification;
import com.prgrms.ijuju.domain.notification.entity.NotificationType;
import com.prgrms.ijuju.domain.notification.repository.NotificationRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final FCMService fcmService;
    private final MemberRepository memberRepository;

    public void createFriendRequestNotification(String senderLoginId, String receiverLoginId) {
        Member sender = memberRepository.findByLoginId(senderLoginId)
                .orElseThrow(() -> new EntityNotFoundException("발신자를 찾을 수 없습니다."));
        Member receiver = memberRepository.findByLoginId(receiverLoginId)
                .orElseThrow(() -> new EntityNotFoundException("수신자를 찾을 수 없습니다."));

        Notification notification = Notification.builder()
                .receiver(receiver)
                .senderLoginId(sender.getId())
                .senderUsername(sender.getUsername())
                .title("친구 요청")
                .content(sender.getUsername() + "님이 친구 요청을 보냈습니다.")
                .type(NotificationType.FRIEND_REQUEST)
                .isRead(false)
                .isDeleted(false)
                .build();

        notificationRepository.save(notification);
        fcmService.sendNotification(notification);
    }

    public Slice<NotificationResponseDto> getNotifications(String loginId, Pageable pageable) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));

        return notificationRepository.findReceiverNotifications(member.getId(), pageable)
                .map(notification -> NotificationResponseDto.of(
                        notification,
                        null
//                        memberImageService.getProfileImageUrl(notification.getSenderLoginId())
                ));
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("알림을 찾을 수 없습니다."));
        notification.markAsRead();
    }

    public void handleFriendRequest(Long notificationId, boolean accept) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("알림을 찾을 수 없습니다."));
        notification.handleFriendRequest(accept);
    }
}
