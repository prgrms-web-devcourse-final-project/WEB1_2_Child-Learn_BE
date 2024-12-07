package com.prgrms.ijuju.domain.chat.handler;

import com.prgrms.ijuju.domain.chat.service.ChatSessionService;
import com.prgrms.ijuju.global.auth.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.lang.NonNull;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ChatSessionService chatSessionService;

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        SecurityUser user = getSecurityUser(session);
        if (user == null) {
            log.error("인증되지 않은 WebSocket 연결 시도");
            closeSession(session);
            return;
        }

        try {
            chatSessionService.connectUser(user.getId(), session.getId());
            log.info("WebSocket 연결 성공 - 사용자 ID: {}", user.getId());
        } catch (Exception e) {
            log.error("WebSocket 연결 실� - 사용자 ID: {}", user.getId(), e);
            closeSession(session);
        }
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) {
        SecurityUser user = getSecurityUser(session);
        if (user != null) {
            log.debug("메시지 수신 - 사용자 ID: {}, 메시지: {}", user.getId(), message.getPayload());
            chatSessionService.heartbeat(user.getId());
        }
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        SecurityUser user = getSecurityUser(session);
        if (user != null) {
            log.info("WebSocket 연결 종료 - 사용자 ID: {}, 상태: {}", user.getId(), status);
            chatSessionService.disconnectUser(user.getId());
        }
    }

    @Override
    public void handleTransportError(@NonNull WebSocketSession session, @NonNull Throwable exception) {
        SecurityUser user = getSecurityUser(session);
        if (user != null) {
            log.error("WebSocket 전송 오류 - 사용자 ID: {}", user.getId(), exception);
            chatSessionService.disconnectUser(user.getId());
        }
    }

    private SecurityUser getSecurityUser(WebSocketSession session) {
        return (SecurityUser) session.getAttributes().get("user");
    }

    private void closeSession(WebSocketSession session) {
        try {
            session.close();
        } catch (IOException e) {
            log.error("세션 종료 실패", e);
        }
    }
} 
