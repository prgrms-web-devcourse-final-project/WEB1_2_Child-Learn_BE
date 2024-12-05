package com.prgrms.ijuju.domain.notification.service;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.exception.MemberException;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.notification.entity.FCMToken;
import com.prgrms.ijuju.domain.notification.repository.FCMTokenRepository;
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
public class FCMTokenService {
    private final FCMTokenRepository fcmTokenRepository;
    private final MemberRepository memberRepository;

    public void saveFCMToken(Long loginId, String tokenValue) {
        log.info("saveFCMToken 메서드 호출");

        Member member = memberRepository.findById(loginId)
                .orElseThrow(MemberException.MEMBER_NOT_FOUND::getMemberTaskException);

        Optional<FCMToken> existingToken = fcmTokenRepository.findByTokenValueAndMember(tokenValue, member);
        FCMToken fcmToken;
        if (existingToken.isPresent()) {
            fcmToken = existingToken.get();
            log.info("기존 토큰이 존재합니다. 기존 토큰을 업데이트합니다. {}", fcmToken);
            fcmToken.updateExpirationDate(LocalDate.now().plusMonths(2));
        } else {
            fcmToken = FCMToken.builder()
                    .tokenValue(tokenValue)
                    .expirationDate(LocalDate.now().plusMonths(1))
                    .member(member)
                    .build();
            log.info("새로운 토큰을 저장합니다. {}", fcmToken);
        }
        fcmTokenRepository.save(fcmToken);
    }

    public void deleteToken(FCMToken fcmToken) {
        log.info("deleteToken 메서드 호출");
        fcmTokenRepository.delete(fcmToken);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void removeExpiredTokens() {
        log.info("removeExpiredTokens 메서드 호출");
        fcmTokenRepository.deleteExpiredTokens();
    }
}
