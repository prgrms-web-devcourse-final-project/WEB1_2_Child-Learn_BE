package com.prgrms.ijuju.global.config;

import com.prgrms.ijuju.domain.stock.adv.advancedinvest.handler.AdvancedInvestWebSocketHandler;
import com.prgrms.ijuju.domain.chat.handler.ChatWebSocketHandler;
import com.prgrms.ijuju.global.auth.JwtHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.lang.NonNull;

@Configuration
@EnableWebSocketMessageBroker
@EnableWebSocket
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer, WebSocketConfigurer {

    private final AdvancedInvestWebSocketHandler advancedInvestWebSocketHandler;
    private final ChatWebSocketHandler chatWebSocketHandler;
    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

    public WebSocketConfig(AdvancedInvestWebSocketHandler advancedInvestWebSocketHandler, ChatWebSocketHandler chatWebSocketHandler, JwtHandshakeInterceptor jwtHandshakeInterceptor) {
        this.advancedInvestWebSocketHandler = advancedInvestWebSocketHandler;
        this.chatWebSocketHandler = chatWebSocketHandler;
        this.jwtHandshakeInterceptor = jwtHandshakeInterceptor;
    }

    // WebSocket 엔드포인트: 상태 관리용
    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
        registry.addHandler(advancedInvestWebSocketHandler, "/api/v1/advanced-invest")
                .setAllowedOrigins("*")
                .addInterceptors(jwtHandshakeInterceptor);
                
        registry.addHandler(chatWebSocketHandler, "/ws")
                .setAllowedOrigins("*");
    }

    // STOMP 엔드포인트: 채팅 메시지용
    @Override
    public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {
       
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    // 메시지 브로커 설정
    @Override
    public void configureMessageBroker(@NonNull MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }
}
