package com.prgrms.ijuju.domain.chat.repository;

import com.prgrms.ijuju.domain.chat.entity.Chat;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<Chat, String> { 

    List<Chat> findByRoomIdOrderByCreatedAtDesc(String roomId);
    
    List<Chat> findByRoomIdAndCreatedAtGreaterThan(String roomId, LocalDateTime since);
    
    List<Chat> findByRoomIdAndIsReadFalseAndSenderIdNot(String roomId, Long senderId);
    
    List<Chat> findByRoomIdAndIsDeletedFalseOrderByCreatedAtDesc(String roomId);
    
    void deleteByRoomId(String roomId);
    
    List<Chat> findByRoomIdAndCreatedAtBetween(
        String roomId, 
        LocalDateTime start, 
        LocalDateTime end
    );
    
    List<Chat> findByRoomIdAndCreatedAtGreaterThanEqual(String roomId, LocalDateTime since);

    Chat findFirstByRoomIdOrderByCreatedAtDesc(String roomId);
}
