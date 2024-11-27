package com.prgrms.ijuju.domain.friend.repository;

import com.prgrms.ijuju.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Member, Long> {
    List<Member> findByUsernameContaining(String username);
} 
