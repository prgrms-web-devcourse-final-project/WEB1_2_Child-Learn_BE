package com.prgrms.ijuju.domain.avatar.repository;

import com.prgrms.ijuju.domain.avatar.entity.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    Optional<Avatar> findByMemberId(Long memberId);
}
