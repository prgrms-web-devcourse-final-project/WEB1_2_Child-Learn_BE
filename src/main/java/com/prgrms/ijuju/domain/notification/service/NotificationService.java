package com.prgrms.ijuju.domain.notification.service;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.exception.MemberErrorCode;
import com.prgrms.ijuju.domain.member.exception.MemberException;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.notification.dto.response.NotificationResponseDto;
import com.prgrms.ijuju.domain.notification.dto.response.SseNotificationResponseDto;
import com.prgrms.ijuju.domain.notification.entity.Notification;
import com.prgrms.ijuju.domain.notification.entity.NotificationType;
import com.prgrms.ijuju.domain.notification.exception.NotificationErrorCode;
import com.prgrms.ijuju.domain.notification.exception.NotificationException;
import com.prgrms.ijuju.domain.notification.repository.EmitterRepository;
import com.prgrms.ijuju.domain.notification.repository.NotificationRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private final EmitterRepository emitterRepository;
    private final SseNotificationService sseNotificationService;

    public Slice<NotificationResponseDto> getNotifications(String loginId, Pageable pageable) {
        log.info("알림 조회");
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        return notificationRepository.findReceiverNotifications(member.getId(), pageable)
                .map(notification -> NotificationResponseDto.of(notification, null));
    }

    public void createFriendRequestNotification(String senderLoginId, String receiverUsername) {
        log.info("친구 요청 알림 생성 senderLoginId: {}, receiverUsername: {}", senderLoginId, receiverUsername);
        Member sender = memberRepository.findByLoginId(senderLoginId)
                .orElseThrow(() -> new NotificationException(NotificationErrorCode.SENDER_NOT_FOUND));
        Member receiver = memberRepository.findByUsername(receiverUsername)
                .orElseThrow(() -> new NotificationException(NotificationErrorCode.RECEIVER_NOT_FOUND));

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

        String eventId = receiverUsername + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByMemberId(receiverUsername);
        emitters.forEach((key, emitter) -> {
            if (key.startsWith(receiverUsername)) {
                sseNotificationService.sendNotification(emitter, eventId, SseNotificationResponseDto.of(notification, null));
            }
        });
    }

    public void createFriendAcceptNotification(String senderLoginId, String receiverUsername) {
        log.info("친구 요청 수락 알림 생성 senderLoginId: {}, receiverUsername: {}", senderLoginId, receiverUsername);
        Member sender = memberRepository.findByLoginId(senderLoginId)
                .orElseThrow(() -> new NotificationException(NotificationErrorCode.SENDER_NOT_FOUND));
        Member receiver = memberRepository.findByUsername(receiverUsername)
                .orElseThrow(() -> new NotificationException(NotificationErrorCode.RECEIVER_NOT_FOUND));

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

        String eventId = receiverUsername + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByMemberId(receiverUsername);
        emitters.forEach((key, emitter) -> {
            if (key.startsWith(receiverUsername)) {
                sseNotificationService.sendNotification(emitter, eventId, SseNotificationResponseDto.of(notification, null));
            }
        });
    }

    public void markAsRead(Long notificationId, Long userId) {
        log.info("알림 읽음 처리");
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationException(NotificationErrorCode.NOTIFICATION_NOT_FOUND));

        // 알림의 수신자와 현재 로그인한 사용자가 동일한지 확인
        if (!notification.getReceiver().getId().equals(userId)) {
            throw new NotificationException(NotificationErrorCode.NOTIFICATION_ACCESS_DENIED);
        }

        notification.markAsRead();

        // SSE로 읽음 상태 변경 이벤트 전송
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        String username = member.getUsername();

        String eventId = username + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = emitterRepository
                .findAllEmitterStartWithByMemberId(username);

        emitters.forEach((key, emitter) -> {
            sseNotificationService.sendNotification(emitter, eventId,
                    Map.of(
                            "type", "READ",
                            "notificationId", notificationId
                    ));
        });
    }

    public void markAllAsRead(Long memberId) {
        log.info("모든 알림 읽음 처리");
        notificationRepository.markAllAsRead(memberId);

        // SSE로 전체 읽음 상태 변경 이벤트 전송
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        String username = member.getUsername();

        String eventId = username + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = emitterRepository
                .findAllEmitterStartWithByMemberId(username);

        emitters.forEach((key, emitter) -> {
            sseNotificationService.sendNotification(emitter, eventId,
                    Map.of(
                            "type", "READ_ALL",
                            "memberId", memberId
                    ));
        });
    }

    public void markAsDeleted(Long notificationId, Long memberId) {
        log.info("알림 삭제");
        if (!notificationRepository.existsById(notificationId)) {
            throw new NotificationException(NotificationErrorCode.NOTIFICATION_NOT_FOUND);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        String username = member.getUsername();

        // 기존 삭제 처리
        notificationRepository.markAsDeleted(notificationId, memberId);

        // SSE로 삭제 이벤트 전송
        String eventId = username + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = emitterRepository
                .findAllEmitterStartWithByMemberId(username);

        log.info("Found {} emitters for memberId {}", emitters.size(), username);

        emitters.forEach((key, emitter) -> {
            log.info("Sending delete notification to emitter with key {}", key);
            sseNotificationService.sendNotification(emitter, eventId,
                    Map.of("type", "DELETE", "notificationId", notificationId));
        });
    }

    // 매일 자정에 실행
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void autoSoftDelete() {
        LocalDateTime expirationDate = LocalDateTime.now().minusDays(30); // 30일 이전 알림

        notificationRepository.markAsDeletedBeforeDate(expirationDate);
    }
}
