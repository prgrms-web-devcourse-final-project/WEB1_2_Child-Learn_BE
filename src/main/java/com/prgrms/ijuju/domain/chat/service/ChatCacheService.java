package com.prgrms.ijuju.domain.chat.service;

import com.prgrms.ijuju.domain.chat.dto.response.ChatMessageResponseDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatCacheService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String RECENT_MESSAGES_KEY = "chat:recent:";

    // 최근 메시지 캐싱
    public void cacheRecentMessages(String roomId, List<ChatMessageResponseDTO> messages, int ttlMinutes) {
        String key = RECENT_MESSAGES_KEY + roomId;
        redisTemplate.opsForList().rightPushAll(key, messages.toArray());
        redisTemplate.expire(key, ttlMinutes, TimeUnit.MINUTES);
    }

    // 캐시된 최근 메시지 조회
    public List<ChatMessageResponseDTO> getRecentMessages(String roomId) {
        String key = RECENT_MESSAGES_KEY + roomId;
        List<Object> cachedMessages = redisTemplate.opsForList().range(key, 0, -1);
        
        if (cachedMessages != null && !cachedMessages.isEmpty()) {
            return cachedMessages.stream()
                .map(msg -> (ChatMessageResponseDTO) msg)
                .collect(Collectors.toList());
        }
        return null;
    }

    // 캐시 무효화
    public void invalidateCache(String roomId) {
        String key = RECENT_MESSAGES_KEY + roomId;
        redisTemplate.delete(key);
    }
} 