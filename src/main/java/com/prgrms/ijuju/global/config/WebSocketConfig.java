package com.prgrms.ijuju.global.config;

import com.prgrms.ijuju.domain.stock.adv.advancedinvest.handler.AdvancedInvestWebSocketHandler;
import com.prgrms.ijuju.global.auth.JwtHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.lang.NonNull;

@Configuration
@EnableWebSocketMessageBroker
@EnableWebSocket
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer, WebSocketConfigurer {

    private final AdvancedInvestWebSocketHandler advancedInvestWebSocketHandler;
    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

    public WebSocketConfig(AdvancedInvestWebSocketHandler advancedInvestWebSocketHandler, JwtHandshakeInterceptor jwtHandshakeInterceptor) {
        this.advancedInvestWebSocketHandler = advancedInvestWebSocketHandler;
        this.jwtHandshakeInterceptor = jwtHandshakeInterceptor;
    }


    //핸들러 방식
    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
        registry.addHandler(advancedInvestWebSocketHandler, "/api/v1/advanced-invest")
                .setAllowedOrigins("*")
                .addInterceptors(jwtHandshakeInterceptor); // 핸들러 URL 등록
    }
  
    public void configureMessageBroker(@NonNull MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*")
                .withSockJS();
    }
    
}