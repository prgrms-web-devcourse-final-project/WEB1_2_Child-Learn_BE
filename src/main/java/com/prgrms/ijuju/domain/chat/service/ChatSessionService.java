package com.prgrms.ijuju.domain.chat.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import com.prgrms.ijuju.domain.chat.exception.ChatException;

import jakarta.annotation.PostConstruct;

import com.prgrms.ijuju.domain.chat.exception.ChatErrorCode;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatSessionService { // Redis를 이용한 세션 관리 서비스
    
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisConnectionFactory redisConnectionFactory;
    
    private static final String USER_SESSION_KEY = "chat:session:";
    private static final String USER_STATUS_KEY = "chat:status:";
    private static final int SESSION_TIMEOUT = 30;
    private static final int HEARTBEAT_TIMEOUT = 90; 

    @PostConstruct
    public void init() {
        validateRedisConnection();
    }
    
    private void validateRedisConnection() {
        try {
            redisConnectionFactory.getConnection().ping();
            log.info("Redis 연결 성공");
        } catch (Exception e) {
            log.error("Redis 연결 실패: {}", e.getMessage());
            throw new ChatException(ChatErrorCode.REDIS_CONNECTION_ERROR);
        }
    }
    
    public void connectUser(Long userId, String sessionId) {
        saveSession(userId, sessionId);
        updateUserStatus(userId, true);
        log.info("User {} connected with session {}", userId, sessionId);
    }

    public void disconnectUser(Long userId) {
        removeSession(userId);
        updateUserStatus(userId, false);
        log.info("User {} disconnected", userId);
    }

    public boolean isUserOnline(Long userId) {
        Boolean status = (Boolean) redisTemplate.opsForValue().get(USER_STATUS_KEY + userId);
        return Boolean.TRUE.equals(status);
    }

    private void saveSession(Long userId, String sessionId) {
        String key = USER_SESSION_KEY + userId;
        redisTemplate.opsForValue().set(key, sessionId);
        redisTemplate.expire(key, SESSION_TIMEOUT, TimeUnit.MINUTES);
    }

    private void updateUserStatus(Long userId, boolean isOnline) {
        String key = USER_STATUS_KEY + userId;
        redisTemplate.opsForValue().set(key, isOnline);
        redisTemplate.expire(key, SESSION_TIMEOUT, TimeUnit.MINUTES);
    }

    public void removeSession(Long userId) {
        redisTemplate.delete(USER_SESSION_KEY + userId);
    }

    public String getSession(Long userId) {
        return (String) redisTemplate.opsForValue().get(USER_SESSION_KEY + userId);
    }

    public void heartbeat(Long userId) {
        try {
            String key = "user:heartbeat:" + userId;
            redisTemplate.opsForValue().set(key, System.currentTimeMillis());
            redisTemplate.expire(key, HEARTBEAT_TIMEOUT, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("하트비트 업데이트 실패: {}", e.getMessage());
            throw new ChatException(ChatErrorCode.REDIS_CONNECTION_ERROR);
        }
    }

    public boolean testConnection() {
        try {
            String testKey = "test:connection:" + System.currentTimeMillis();
            redisTemplate.opsForValue().set(testKey, "test");
            redisTemplate.delete(testKey);
            log.info("Redis 연결 테스트 성공");
            return true;
        } catch (Exception e) {
            log.error("Redis 연결 테스트 실패: {}", e.getMessage());
            return false;
        }
    }
}
