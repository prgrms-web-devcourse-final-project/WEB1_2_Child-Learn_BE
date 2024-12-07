package com.prgrms.ijuju.domain.chat.repository;

import com.prgrms.ijuju.domain.chat.entity.Chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    
    List<Chat> findByRoomIdAndCreatedAtBetween(String roomId, LocalDateTime start, LocalDateTime end);
    
    List<Chat> findByRoomIdAndCreatedAtGreaterThanEqual(String roomId, LocalDateTime since);

    Chat findFirstByRoomIdOrderByCreatedAtDesc(String roomId);

    Page<Chat> findByRoomIdOrderByCreatedAtDesc(String roomId, Pageable pageable);
    
    Page<Chat> findByRoomIdAndCreatedAtGreaterThanOrderByCreatedAtDesc(String roomId, LocalDateTime since, Pageable pageable);
    
    Page<Chat> findByRoomIdAndIsReadFalseAndSenderIdNotOrderByCreatedAtDesc(String roomId, Long senderId, Pageable pageable);

    Page<Chat> findByRoomIdAndCreatedAtBeforeOrderByCreatedAtDesc(String roomId, LocalDateTime createdAt, Pageable pageable);
}
