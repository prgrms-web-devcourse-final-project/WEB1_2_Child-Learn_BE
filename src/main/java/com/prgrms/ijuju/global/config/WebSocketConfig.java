package com.prgrms.ijuju.global.config;

import com.prgrms.ijuju.domain.stock.adv.advancedinvest.handler.AdvancedInvestWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.lang.NonNull;

@Configuration
@EnableWebSocketMessageBroker
@EnableWebSocket
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer, WebSocketConfigurer {

    private final AdvancedInvestWebSocketHandler advancedInvestWebSocketHandler;

    public WebSocketConfig(AdvancedInvestWebSocketHandler advancedInvestWebSocketHandler) {
        this.advancedInvestWebSocketHandler = advancedInvestWebSocketHandler;
    }

    //핸들러 방식
    @Override

    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
        registry.addHandler(advancedInvestWebSocketHandler, "api/v1/advanced-invest")
                .setAllowedOrigins("*"); // 핸들러 URL 등록
    }
  
    public void configureMessageBroker(@NonNull MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub");
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {
        registry.addEndpoint("/ws", "/ws-stomp")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
    
}