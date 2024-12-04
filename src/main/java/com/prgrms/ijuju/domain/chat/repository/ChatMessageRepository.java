package com.prgrms.ijuju.domain.chat.repository;

import com.prgrms.ijuju.domain.chat.entity.Chat;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<Chat, String> { // MongoDB를 이용한 채팅 메시지 저장소

    List<Chat> findByRoomIdOrderByCreatedAtDesc(Long roomId);
    
    List<Chat> findByRoomIdAndCreatedAtGreaterThan(Long roomId, LocalDateTime since);
    
    List<Chat> findByRoomIdAndIsReadFalseAndSenderIdNot(Long roomId, Long senderId);
    
    List<Chat> findByRoomIdAndIsDeletedFalseOrderByCreatedAtDesc(Long roomId);
    
    void deleteByRoomId(Long roomId);
    
    List<Chat> findByRoomIdAndCreatedAtBetween(
        Long roomId, 
        LocalDateTime start, 
        LocalDateTime end
    );
    
    List<Chat> findByChatRoomIdAndCreatedAtGreaterThanEqual(Long roomId, LocalDateTime since);
}
