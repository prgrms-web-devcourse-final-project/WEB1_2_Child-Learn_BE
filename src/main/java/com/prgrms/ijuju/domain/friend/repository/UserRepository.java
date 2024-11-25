package com.prgrms.ijuju.domain.friend.repository;

import com.prgrms.ijuju.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<Member, Long> { // 사용자
    List<Member> findByUsernameContaining(String username);
} 