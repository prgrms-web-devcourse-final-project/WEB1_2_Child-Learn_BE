package com.prgrms.ijuju.domain.notification.service;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.exception.MemberErrorCode;
import com.prgrms.ijuju.domain.member.exception.MemberException;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.notification.repository.EmitterRepository;
import com.prgrms.ijuju.domain.notification.repository.NotificationRepository;
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
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60; // 지속 시간 1시간

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private final EmitterRepository emitterRepository;

    public SseEmitter subscribe(String memberId, String lastEventId) {
        Member member = memberRepository.findByLoginId(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        String username = member.getUsername();
        String emitterId = username + "_" + System.currentTimeMillis(); //고유 아이디 생성
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT); // 현재 클라이언트를 위한 SSeEmitter 객체 생성

        emitterRepository.save(emitterId, sseEmitter);
        log.info(" 멤버아이디{} - 새로운 SSE emitter added :  {}", memberId, sseEmitter);
        log.info("lastEventId : {}", lastEventId);

        // 시간 초과나 비동기 요청이 안되면 자동으로 삭제
        sseEmitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        sseEmitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
        sseEmitter.onError((e) -> emitterRepository.deleteById(emitterId));

        // 최초 연결시 더미데이터가 없으면 503오류 발생 503 에러 방지를 위한 더미 이벤트 전송
        sendNotification(sseEmitter, emitterId, "EventStream Created. [username=" + username + "]");

        // 클라이언트가 미수신한 이벤트 재전송
        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithByMemberId(username);
            events.entrySet().stream()
                    .filter(entry -> entry.getKey().startsWith(lastEventId))
                    .forEach(entry -> sendNotification(sseEmitter, entry.getKey(), entry.getValue()));
        }

        return sseEmitter;
    }

    public void sendNotification(SseEmitter emitter, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(emitterId)
                    .name("notification")
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
            log.error("SSE 연결 에러: {}", exception.getMessage());
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
