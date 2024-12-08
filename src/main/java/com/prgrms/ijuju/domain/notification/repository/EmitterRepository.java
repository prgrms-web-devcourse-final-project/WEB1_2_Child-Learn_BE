package com.prgrms.ijuju.domain.notification.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class EmitterRepository {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, CacheEntry> eventCache = new ConcurrentHashMap<>();
    private static final long EVENT_CACHE_EXPIRY = 30L * 60 * 1000; // 30분
    private static final int INITIAL_CAPACITY = 100;
    private static final float LOAD_FACTOR = 0.75f;

    // 캐시 엔트리를 위한 내부 클래스
    private static class CacheEntry {
        private final Object data;
        private final long timestamp;

        public CacheEntry(Object data) {
            this.data = data;
            this.timestamp = System.currentTimeMillis();
        }

        public boolean isExpired() {
            return System.currentTimeMillis() - timestamp > EVENT_CACHE_EXPIRY;
        }

        public Object getData() {
            return data;
        }
    }

    public void save(String emitterId, SseEmitter emitter) {
        log.info("save emitter: {}", emitterId);
        emitters.put(emitterId, emitter);
    }

    public void saveEventCache(String eventCacheId, Object event) {
        // 새로운 항목 추가 전에 만료된 항목 정리 (임계값 기반)
        if (eventCache.size() >= INITIAL_CAPACITY * LOAD_FACTOR) {
            cleanUpExpiredCache();
        }
        eventCache.put(eventCacheId, new CacheEntry(event));
    }

    private void cleanUpExpiredCache() {
        eventCache.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }

    public Map<String, SseEmitter> findAllEmitterStartWithByMemberId(String memberId) {
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(memberId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<String, Object> findAllEventCacheStartWithByMemberId(String memberId) {
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(memberId))
                .filter(entry -> !entry.getValue().isExpired())  // 만료된 항목 필터링
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().getData()
                ));
    }

    public void deleteById(String emitterId) {
        emitters.remove(emitterId);
    }

    public void deleteAllEmitterStartWithId(String memberId) {
        emitters.forEach((key, emitter) -> {
            if (key.startsWith(memberId)) {
                emitters.remove(key);
            }
        });
    }

    public void deleteAllEventCacheStartWithId(String memberId) {
        eventCache.entrySet().removeIf(entry ->
                entry.getKey().startsWith(memberId) || entry.getValue().isExpired()
        );
    }
}
