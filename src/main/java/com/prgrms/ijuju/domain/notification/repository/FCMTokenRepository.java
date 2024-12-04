package com.prgrms.ijuju.domain.notification.repository;

import com.prgrms.ijuju.domain.notification.entity.FCMToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FCMTokenRepository extends JpaRepository<FCMToken, Long> {
}
