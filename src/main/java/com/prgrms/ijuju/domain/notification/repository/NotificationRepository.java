package com.prgrms.ijuju.domain.notification.repository;

import com.prgrms.ijuju.domain.notification.entity.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n " +
            "WHERE n.receiver.id = :receiverId " +
            "AND n.isDeleted = false " +
            "ORDER BY n.createdAt DESC")
    Slice<Notification> findReceiverNotifications(
            @Param("receiverId") Long receiverId,
            Pageable pageable);
}
