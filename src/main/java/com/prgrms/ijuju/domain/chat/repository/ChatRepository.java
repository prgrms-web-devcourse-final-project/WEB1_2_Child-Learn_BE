package com.prgrms.ijuju.domain.chat.repository;

import com.prgrms.ijuju.domain.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    
    List<Chat> findByChatRoomIdAndIsReadFalseAndSenderIdNot(Long roomId, Long senderId);

    int countByChatRoomIdAndSenderIdNotAndIsReadFalseAndIsDeletedFalse(Long roomId, Long senderId);

    List<Chat> findByChatRoomIdAndSenderIdNotAndIsReadFalseAndIsDeletedFalse(Long roomId, Long senderId);

    List<Chat> findByChatRoomIdOrderByCreatedAtDesc(Long roomId);

    List<Chat> findByChatRoomIdOrderByCreatedAtAsc(Long chatRoomId);
} 
