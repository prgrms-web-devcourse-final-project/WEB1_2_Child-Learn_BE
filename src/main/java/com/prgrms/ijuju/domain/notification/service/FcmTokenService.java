package com.prgrms.ijuju.domain.notification.service;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.exception.MemberException;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.notification.entity.FcmToken;
import com.prgrms.ijuju.domain.notification.repository.FcmTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FcmTokenService {
    private final FcmTokenRepository fcmTokenRepository;
    private final MemberRepository memberRepository;

    public void saveFCMToken(Long loginId, String tokenValue) {
        log.info("saveFCMToken 메서드 호출");

        Member member = memberRepository.findById(loginId)
                .orElseThrow(MemberException.MEMBER_NOT_FOUND::getMemberTaskException);

        Optional<FcmToken> existingToken = fcmTokenRepository.findByTokenValueAndMember(tokenValue, member);
        FcmToken fcmToken;
        if (existingToken.isPresent()) {
            fcmToken = existingToken.get();
            log.info("기존 토큰이 존재합니다. 기존 토큰을 업데이트합니다. {}", fcmToken);
            fcmToken.updateExpirationDate(LocalDate.now().plusMonths(2));
        } else {
            fcmToken = FcmToken.builder()
                    .tokenValue(tokenValue)
                    .expirationDate(LocalDate.now().plusMonths(1))
                    .member(member)
                    .build();
            log.info("새로운 토큰을 저장합니다. {}", fcmToken);
        }
        fcmTokenRepository.save(fcmToken);
    }

    public void deleteToken(String fcmToken) {
        log.info("FCM 토큰 삭제 시작: {}", fcmToken);

        try {
            FcmToken tokenEntity = fcmTokenRepository.findByTokenValue(fcmToken)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 FCM 토큰입니다."));

            fcmTokenRepository.delete(tokenEntity);
            log.info("FCM 토큰 삭제 완료: {}", fcmToken);
        } catch (Exception e) {
            log.error("FCM 토큰 삭제 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("토큰 삭제 중 오류가 발생했습니다.", e);
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void removeExpiredTokens() {
        log.info("removeExpiredTokens 메서드 호출");
        fcmTokenRepository.deleteExpiredTokens();
    }
}
