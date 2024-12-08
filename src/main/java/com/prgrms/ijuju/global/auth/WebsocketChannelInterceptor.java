package com.prgrms.ijuju.global.auth;

import com.prgrms.ijuju.global.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebsocketChannelInterceptor implements ChannelInterceptor {
    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7); // "Bearer " 제거
                try {
                    // JwtUtil을 통해 JWT 토큰 검증 및 클레임 추출
                    Claims claims = jwtUtil.decode(token);
                    log.info("JWT 검증 성공: {}", claims);

                    Long memberId = Long.valueOf(claims.get("data", Map.class).get("id").toString());
                    accessor.getSessionAttributes().put("AUTHENTICATED_MEMBER_ID", memberId);

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(memberId, null, Collections.emptyList());
                    accessor.setUser(authToken);

                } catch (Exception e) {
                    log.error("JWT 검증 실패: {}", e.getMessage());
                    return null; // 인증 실패 시 메시지 차단
                }
            } else {
                log.error("유효한 토큰을 찾을 수 없음");
                return null; // 토큰 없거나 형식 잘못됨
            }
        } else {
            Object memberId = accessor.getSessionAttributes().get("AUTHENTICATED_MEMBER_ID");
            if (memberId == null) {
                log.error("인증 정보가 없는 사용자 메시지 차단");
                return null;
            }
        }

        return message;
    }
}
