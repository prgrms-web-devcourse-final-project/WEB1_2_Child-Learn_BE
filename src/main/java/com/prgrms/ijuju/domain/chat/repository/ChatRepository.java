package com.prgrms.ijuju.domain.chat.repository;

import com.prgrms.ijuju.domain.chat.entity.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends MongoRepository<Chat, String> {
    
    List<Chat> findByRoomIdAndIsReadFalseAndSenderIdNot(Long roomId, Long senderId);

    int countByRoomIdAndSenderIdNotAndIsReadFalseAndIsDeletedFalse(Long roomId, Long senderId);

    List<Chat> findByRoomIdAndSenderIdNotAndIsReadFalseAndIsDeletedFalse(Long roomId, Long senderId);

    List<Chat> findByRoomIdOrderByCreatedAtDesc(Long roomId);

    List<Chat> findByRoomIdOrderByCreatedAtAsc(Long roomId);
} 
