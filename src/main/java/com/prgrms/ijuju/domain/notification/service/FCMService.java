package com.prgrms.ijuju.domain.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.exception.MemberException;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.notification.entity.FCMToken;
import com.prgrms.ijuju.domain.notification.entity.Notification;
import com.prgrms.ijuju.domain.notification.repository.FCMTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FCMService {
    private final MemberRepository memberRepository;
    private final FCMTokenRepository fcmTokenRepository;
    private final FirebaseMessaging firebaseMessaging;


    public void sendNotification(Notification notification) {
        Member member = memberRepository.findById(notification.getReceiver().getId())
                .orElseThrow(MemberException.MEMBER_NOT_FOUND::getMemberTaskException);

        List<FCMToken> tokens = member.getFcmTokens();
        // 실패한 토큰 리스트 초기화
        List<FCMToken> faliedTokens = new ArrayList<>();

        for (FCMToken token : tokens) {
            try{
                Map<String, String> data = new HashMap<>();
                data.put("type", notification.getType().name());
                data.put("senderLoginId", notification.getSenderLoginId().toString());
                data.put("senderUsername", notification.getSenderUsername());
                data.put("notificationId", notification.getId().toString());
                data.put("title", notification.getTitle());
                data.put("content", notification.getContent());
                data.put("createdAt", notification.getCreatedAt().toString());

                Message message = Message.builder()
                        .setToken(token.getTokenValue())
                        .setNotification(com.google.firebase.messaging.Notification.builder() // 알림 메시지 설정
                                .setTitle(notification.getTitle())
                                .setBody(notification.getContent())
                                .build())
                        .putAllData(data)
                        .build();
                // FCM 메시지 전송
                firebaseMessaging.send(message);
            } catch (FirebaseMessagingException e) {
                faliedTokens.add(token);
                log.error("FCM 메시지 전송 실패: {}", e.getMessage());
            }
        }
        // 실패한 토큰 삭제
        if (!faliedTokens.isEmpty()) {
            fcmTokenRepository.deleteAll(faliedTokens);
        }
    }
}
