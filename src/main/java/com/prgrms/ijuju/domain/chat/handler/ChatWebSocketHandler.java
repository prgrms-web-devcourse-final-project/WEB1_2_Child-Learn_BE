package com.prgrms.ijuju.domain.chat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.ijuju.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final ChatService chatService;
    
    // 연결된 세션을 저장하는 맵 (세션ID, WebSocketSession)
    private static final ConcurrentHashMap<String, WebSocketSession> SESSIONS = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 웹소켓 연결이 성공했을 때
        String sessionId = session.getId();
        SESSIONS.put(sessionId, session);
        log.info("새로운 웹소켓 연결 성공: {}", sessionId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 클라이언트로부터 메시지를 받았을 때
        String payload = message.getPayload();
        log.info("받은 메시지: {}", payload);

        try {
            // 모든 연결된 세션에 메시지 브로드캐스트
            for (WebSocketSession clientSession : SESSIONS.values()) {
                if (clientSession.isOpen()) {
                    clientSession.sendMessage(new TextMessage(payload));
                }
            }
        } catch (Exception e) {
            log.error("메시지 처리 중 오류 발생: ", e);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // 웹소켓 통신 중 에러가 발생했을 때
        log.error("웹소켓 통신 에러: {}", session.getId(), exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 웹소켓 연결이 종료되었을 때
        String sessionId = session.getId();
        SESSIONS.remove(sessionId);
        log.info("웹소켓 연결 종료: {}", sessionId);
    }

    // 특정 세션에 메시지 전송
    public void sendMessageToSession(String sessionId, String message) {
        WebSocketSession session = SESSIONS.get(sessionId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (Exception e) {
                log.error("메시지 전송 중 오류 발생: ", e);
            }
        }
    }

    // 현재 연결된 모든 세션에 메시지 전송
    public void broadcastMessage(String message) {
        SESSIONS.values().forEach(session -> {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (Exception e) {
                    log.error("브로드캐스트 메시지 전송 중 오류 발생: ", e);
                }
            }
        });
    }
} 