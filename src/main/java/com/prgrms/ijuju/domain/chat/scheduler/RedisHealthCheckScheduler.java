package com.prgrms.ijuju.domain.chat.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisHealthCheckScheduler {
    
    private final RedisTemplate<String, Object> redisTemplate;
    
    @Scheduled(fixedRate = 300000) // 5분마다 실행
    public void checkRedisHealth() {
        try {
            redisTemplate.opsForValue().get("health:check");
            log.info("Redis 연결 상태 정상");
        } catch (Exception e) {
            log.error("Redis 연결 오류 발생: {}", e.getMessage());
        }
    }
} 