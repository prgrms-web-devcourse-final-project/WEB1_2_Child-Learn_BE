package com.prgrms.ijuju.domain.notification.repository;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.notification.entity.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    Optional<FcmToken> findByTokenValueAndMember(String tokenValue, Member member);

    // 회원의 유효한 토큰 목록 조회
    @Query("SELECT t FROM FcmToken t WHERE t.member.id = :memberId AND t.expirationDate >= CURRENT_DATE")
    List<FcmToken> findValidTokensByMemberId(@Param("memberId") Long memberId);

    // 특정 토큰값으로 조회
    Optional<FcmToken> findByTokenValue(String tokenValue);

    // 만료된 토큰 삭제
    @Modifying
    @Query("DELETE FROM FcmToken t WHERE t.expirationDate < CURRENT_DATE")
    void deleteExpiredTokens();

    // 회원의 가장 최근 토큰 조회
    @Query("SELECT t FROM FcmToken t WHERE t.member.id = :memberId ORDER BY t.createdAt DESC")
    Optional<FcmToken> findLatestByMemberId(@Param("memberId") Long memberId);

    // 특정 회원의 모든 토큰 삭제
    @Modifying
    @Query("DELETE FROM FcmToken t WHERE t.member.id = :memberId")
    void deleteAllByMemberId(@Param("memberId") Long memberId);
}
