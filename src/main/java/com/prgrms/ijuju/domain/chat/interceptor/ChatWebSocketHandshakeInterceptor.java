package com.prgrms.ijuju.domain.chat.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.prgrms.ijuju.global.util.JwtUtil;
import io.jsonwebtoken.Claims;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response,
                                 @NonNull WebSocketHandler wsHandler, @NonNull Map<String, Object> attributes) {
        try {
            String jwtToken = request.getHeaders().getFirst("Authorization");
            log.debug("Authorization 헤더: {}", jwtToken);

            if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
                jwtToken = jwtToken.substring(7);
                Claims claims = JwtUtil.decode(jwtToken);
                attributes.put("userClaims", claims);
                log.info("Chat WebSocket 연결 성공: {}", claims);
                return true;
            }

            log.warn("Chat JWT 토큰이 요청에 포함되지 않았습니다.");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;

        } catch (Exception e) {
            log.error("Chat JWT 검증 실패: {}", e.getMessage());
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
    }

    @Override
    public void afterHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response,
                             @NonNull WebSocketHandler wsHandler, @Nullable Exception exception) {
        log.info("Chat WebSocket Handshake 완료");
    }
} 