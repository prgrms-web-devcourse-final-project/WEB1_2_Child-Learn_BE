package com.prgrms.ijuju.domain.notification.repository;

import com.prgrms.ijuju.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
