package com.prgrms.ijuju.domain.notification.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class EmitterRepository {
    // ConcurrentHashMap을 사용하여 thread-safe 보장
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    public SseEmitter save(String emitterId, SseEmitter emitter) {
        log.info("save emitter: {}", emitterId);
        emitters.put(emitterId, emitter);
        return emitter;
    }

    public void saveEventCache(String eventCacheId, Object event) {
        eventCache.put(eventCacheId, event);
    }

    public Map<String, SseEmitter> findAllEmitterStartWithByMemberId(String memberId) {
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(memberId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<String, Object> findAllEventCacheStartWithByMemberId(String memberId) {
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(memberId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
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
        eventCache.forEach((key, value) -> {
            if (key.startsWith(memberId)) {
                eventCache.remove(key);
            }
        });
    }
}
