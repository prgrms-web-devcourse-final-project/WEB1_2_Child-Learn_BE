package com.prgrms.ijuju.domain.notification.repository;

import com.prgrms.ijuju.domain.notification.entity.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n " +
            "WHERE n.receiver.id = :receiverId " +
            "AND n.isDeleted = false " +
            "ORDER BY n.createdAt DESC")
    Slice<Notification> findReceiverNotifications(
            @Param("receiverId") Long receiverId,
            Pageable pageable);

    @Query("UPDATE Notification n " +
            "SET n.isRead = true " +
            "WHERE n.receiver.id = :memberId " +
            "AND n.isDeleted = false " +
            "AND n.isRead = false")
    @Modifying(clearAutomatically = true)
    void markAllAsRead(@Param("memberId") Long memberId);

    @Modifying
    @Query("UPDATE Notification n SET n.isDeleted = true " +
            "WHERE n.createdAt < :expirationDate " +
            "AND n.isDeleted = false")
    void markAsDeletedBeforeDate(@Param("expirationDate") LocalDateTime expirationDate);

    @Modifying
    @Query("UPDATE Notification n SET n.isDeleted = true " +
            "WHERE n.id = :notificationId " +
            "AND n.receiver.id = :memberId " +
            "AND n.isDeleted = false")
    void markAsDeleted(
            @Param("notificationId") Long notificationId,
            @Param("memberId") Long memberId
    );
}
