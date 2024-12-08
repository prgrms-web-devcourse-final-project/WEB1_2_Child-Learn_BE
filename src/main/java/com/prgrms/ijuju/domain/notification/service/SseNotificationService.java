package com.prgrms.ijuju.domain.notification.service;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.exception.MemberErrorCode;
import com.prgrms.ijuju.domain.member.exception.MemberException;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.notification.exception.NotificationErrorCode;
import com.prgrms.ijuju.domain.notification.exception.NotificationException;
import com.prgrms.ijuju.domain.notification.repository.EmitterRepository;
import com.prgrms.ijuju.domain.notification.repository.NotificationRepository;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SseNotificationService {
    private static final Long DEFAULT_TIMEOUT = 5L * 1000 * 60; // 지속 시간 5분
    private static final Long RECONNECTION_TIMEOUT = 3L * 1000; // 재연결 타임아웃 3초

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private final EmitterRepository emitterRepository;

    public SseEmitter subscribe(String memberId, String lastEventId) {
        Member member = memberRepository.findByLoginId(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        String username = member.getUsername();
        String emitterId = username + "_" + System.currentTimeMillis(); //고유 아이디 생성

        // 이전 연결이 있다면 제거
        emitterRepository.deleteAllEmitterStartWithId(username);

        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT); // 현재 클라이언트를 위한 SSeEmitter 객체 생성

        // 연결 직후 재연결 타임아웃 설정
        sseEmitter.onTimeout(() -> {
            emitterRepository.deleteById(emitterId);
            // 재연결 요청
            try {
                sseEmitter.send(SseEmitter.event()
                        .name("retry")
                        .data(""));
            } catch (IOException e) {
                log.error("Failed to send retry event", e);
            }
        });

        // 에러 발생시 즉시 제거
        sseEmitter.onError((e) -> {
            log.error("SSE Error!", e);
            emitterRepository.deleteById(emitterId);
        });

        // 연결 완료시 제거
        sseEmitter.onCompletion(() -> {
            log.info("SSE completed for emitterId: {}", emitterId);
            emitterRepository.deleteById(emitterId);
        });

        emitterRepository.save(emitterId, sseEmitter);

        // 503 에러 방지용 더미 이벤트
        sendNotification(sseEmitter, emitterId, "connected");

        // 미수신한 이벤트 재전송 (최근 10개만)
        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithByMemberId(username);
            events.entrySet().stream()
                    .filter(entry -> entry.getKey().compareTo(lastEventId) > 0)
                    .sorted(Map.Entry.comparingByKey())
                    .limit(10)
                    .forEach(entry -> sendNotification(sseEmitter, entry.getKey(), entry.getValue()));
        }

        return sseEmitter;
    }

    public void sendNotification(SseEmitter emitter, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(emitterId)
                    .name("notification")
                    .data(data)
                    .reconnectTime(RECONNECTION_TIMEOUT));
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
            log.error("SSE 연결 에러: {}", exception.getMessage());
            throw new NotificationException(NotificationErrorCode.SSE_SEND_ERROR);
        }
    }

    public void disconnect(String memberId) {
        log.info("멤버아이디{} - SSE 연결 종료", memberId);
        Member member = memberRepository.findByLoginId(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        String username = member.getUsername();
        // 해당 회원의 모든 emitter 제거
        emitterRepository.deleteAllEmitterStartWithId(username);
        // 해당 회원의 모든 이벤트 캐시 제거
        emitterRepository.deleteAllEventCacheStartWithId(username);
    }

    // 회원 탈퇴시
    public void deleteMemberNotifications(String memberId) {
        Member member = memberRepository.findByLoginId(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        String username = member.getUsername();
        // DB에서 알림 삭제
        notificationRepository.deleteAllByReceiverId(username);

        // SSE 연결 종료 및 캐시 삭제
        emitterRepository.deleteAllEmitterStartWithId(username);
        emitterRepository.deleteAllEventCacheStartWithId(memberId);
    }

    // 알림 설정 변경시
    public void updateNotificationSettings(String memberId) {
        Member member = memberRepository.findByLoginId(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        String username = member.getUsername();
        // 기존 연결 종료 및 캐시 초기화
        emitterRepository.deleteAllEmitterStartWithId(username);
        emitterRepository.deleteAllEventCacheStartWithId(username);

        // 새로운 SSE 연결 생성
        String emitterId = username + "_" + System.currentTimeMillis();
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);

        // 새로운 이미터 저장
        emitterRepository.save(emitterId, sseEmitter);

        // 연결 종료 시 자동 삭제 설정
        sseEmitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        sseEmitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // 연결 설정 완료 이벤트 전송
        sendNotification(sseEmitter, emitterId,
                "Notification settings updated for user: " + username);
    }
}
