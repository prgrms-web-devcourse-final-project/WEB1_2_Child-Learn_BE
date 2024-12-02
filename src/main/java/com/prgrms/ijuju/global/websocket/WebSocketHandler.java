package com.prgrms.ijuju.global.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.ijuju.domain.wallet.dto.response.WalletResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final ConcurrentHashMap<Long, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Long memberId = extractMemberId(session);
        userSessions.put(memberId, session);
        log.info("새로운 웹소켓 연결: memberId={}", memberId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long memberId = extractMemberId(session);
        userSessions.remove(memberId);
        log.info("웹소켓 연결 종료: memberId={}", memberId);
    }

    public void sendPointUpdate(Long memberId, WalletResponseDTO response) {
        WebSocketSession session = userSessions.get(memberId);
        if (session != null && session.isOpen()) {
            try {
                String message = objectMapper.writeValueAsString(response);
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                log.error("포인트 업데이트 메시지 전송 실패: memberId={}", memberId, e);
            }
        }
    }

    private Long extractMemberId(WebSocketSession session) {
        // 세션에서 memberId를 추출하는 로직
        // 예: 세션 속성이나 URI 파라미터에서 memberId를 가져옴
        return Long.parseLong(session.getUri().getQuery().split("=")[1]);
    }
} 