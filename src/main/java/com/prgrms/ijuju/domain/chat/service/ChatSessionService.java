package com.prgrms.ijuju.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatSessionService { // Redis를 이용한 세션 관리 서비스
    
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String USER_SESSION_KEY = "chat:session:";

    public void saveSession(Long userId, String sessionId) {
        redisTemplate.opsForValue().set(USER_SESSION_KEY + userId, sessionId);
    }

    public void removeSession(Long userId) {
        redisTemplate.delete(USER_SESSION_KEY + userId);
    }

    public String getSession(Long userId) {
        return (String) redisTemplate.opsForValue().get(USER_SESSION_KEY + userId);
    }
}
