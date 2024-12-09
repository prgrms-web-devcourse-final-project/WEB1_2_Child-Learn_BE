package com.prgrms.ijuju.domain.chat.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.prgrms.ijuju.domain.chat.service.ChatSessionService;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class HeartbeatCheckScheduler {
    
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatSessionService chatSessionService;
    
    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void checkHeartbeats() {
        String pattern = "user:heartbeat:*";
        Set<String> keys = redisTemplate.keys(pattern);
        
        if (keys != null) {
            for (String key : keys) {
                Long lastHeartbeat = (Long) redisTemplate.opsForValue().get(key);
                if (lastHeartbeat != null) {
                    long timeSinceLastHeartbeat = System.currentTimeMillis() - lastHeartbeat;
                    
                    if (timeSinceLastHeartbeat > TimeUnit.SECONDS.toMillis(90)) { // 90초 초과
                        String userId = key.replace("user:heartbeat:", "");
                        chatSessionService.disconnectUser(Long.valueOf(userId));
                        log.info("사용자 {} 하트비트 타임아웃으로 연결 종료", userId);
                    }
                }
            }
        }
    }
} 