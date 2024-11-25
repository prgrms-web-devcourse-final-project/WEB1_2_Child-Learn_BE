package com.prgrms.ijuju.domain.member.repository;

import com.prgrms.ijuju.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByLoginId(String loginId);
    Optional<Member> findByEmail(String email);
    Optional<Member> findLoginIdByEmailAndBirth(String email, LocalDate birth);
    Optional<Member> findByRefreshToken(String refreshToken);
    Optional<Member> findByLoginIdAndEmail(String loginId, String email);
    Page<Member> findAll(Pageable pageable);
}
