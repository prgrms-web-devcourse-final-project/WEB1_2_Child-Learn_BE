package com.prgrms.ijuju.domain.chat.repository;

import com.prgrms.ijuju.domain.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    int countByChatRoomIdAndSenderIdNotAndIsReadFalseAndIsDeletedFalse(Long roomId, Long senderId);

    List<Chat> findByChatRoomIdAndSenderIdNotAndIsReadFalseAndIsDeletedFalse(Long roomId, Long senderId);

    List<Chat> findByChatRoomIdOrderByCreatedAtDesc(Long roomId);
} 
