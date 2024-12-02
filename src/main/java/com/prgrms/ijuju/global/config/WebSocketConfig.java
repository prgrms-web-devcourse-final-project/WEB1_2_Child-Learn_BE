package com.prgrms.ijuju.global.config;

import com.prgrms.ijuju.domain.stock.adv.advancedinvest.handler.AdvancedInvestWebSocketHandler;
import org.springframework.context.annotation.Configuration;
<<<<<<< Updated upstream
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
=======
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.lang.NonNull;

@Configuration
@EnableWebSocketMessageBroker
@EnableWebSocket
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer, WebSocketConfigurer {
>>>>>>> Stashed changes

    private final AdvancedInvestWebSocketHandler advancedInvestWebSocketHandler;

    public WebSocketConfig(AdvancedInvestWebSocketHandler advancedInvestWebSocketHandler) {
        this.advancedInvestWebSocketHandler = advancedInvestWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(advancedInvestWebSocketHandler, "/api/v1/advanced-invest")
                .setAllowedOrigins("*") // 모든 출처 허용
                .withSockJS(); // SockJS 지원
    }


    //핸들러 방식
    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
        registry.addHandler(advancedInvestWebSocketHandler, "api/v1/advanced-invest")
                .setAllowedOrigins("*"); // 핸들러 URL 등록
    }
}