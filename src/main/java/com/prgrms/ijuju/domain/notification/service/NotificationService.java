package com.prgrms.ijuju.domain.notification.service;

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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final FcmService fcmService;
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

    public void createFriendAcceptNotification(String senderLoginId, String receiverLoginId) {
        Member sender = memberRepository.findByLoginId(senderLoginId)
                .orElseThrow(() -> new EntityNotFoundException("발신자를 찾을 수 없습니다."));
        Member receiver = memberRepository.findByLoginId(receiverLoginId)
                .orElseThrow(() -> new EntityNotFoundException("수신자를 찾을 수 없습니다."));

        Notification notification = Notification.builder()
                .receiver(receiver)
                .senderLoginId(sender.getId())
                .senderUsername(sender.getUsername())
                .title("친구 요청 수락")
                .content(sender.getUsername() + "님이 친구 요청을 수락했습니다.")
                .type(NotificationType.FRIEND_ACCEPT)
                .isRead(false)
                .isDeleted(false)
                .build();

        notificationRepository.save(notification);
        fcmService.sendNotification(notification);
    }

    public void createMessageNotification(String senderLoginId, String receiverLoginId, String messageContent) {
        Member sender = memberRepository.findByLoginId(senderLoginId)
                .orElseThrow(() -> new EntityNotFoundException("발신자를 찾을 수 없습니다."));
        Member receiver = memberRepository.findByLoginId(receiverLoginId)
                .orElseThrow(() -> new EntityNotFoundException("수신자를 찾을 수 없습니다."));

        Notification notification = Notification.builder()
                .receiver(receiver)
                .senderLoginId(sender.getId())
                .senderUsername(sender.getUsername())
                .title(sender.getUsername() + "님이 메시지를 보냈습니다.")
                .content(messageContent)
                .type(NotificationType.MESSAGE)
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

    public void markAsRead(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("알림을 찾을 수 없습니다."));

        // 알림의 수신자와 현재 로그인한 사용자가 동일한지 확인
        if (!notification.getReceiver().getId().equals(userId)) {
            throw new AccessDeniedException("해당 알림에 대한 권한이 없습니다.");
        }

        notification.markAsRead();
    }

    public void markAllAsRead(Long memberId) {
        notificationRepository.markAllAsRead(memberId);
    }

    public void markAsDeleted(Long notificationId, Long memberId) {
        notificationRepository.markAsDeleted(notificationId, memberId);
    }

    // 매일 자정에 실행
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void autoSoftDelete() {
        LocalDateTime expirationDate = LocalDateTime.now().minusDays(30); // 30일 이전 알림

        notificationRepository.markAsDeletedBeforeDate(expirationDate);
    }
}
