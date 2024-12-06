package com.prgrms.ijuju.domain.chat.controller;

import com.prgrms.ijuju.domain.chat.dto.request.ChatMessageRequestDTO;
import com.prgrms.ijuju.domain.chat.dto.request.ChatReadRequestDTO;
import com.prgrms.ijuju.domain.chat.dto.response.ChatMessageResponseDTO;
import com.prgrms.ijuju.domain.chat.dto.response.ChatReadResponseDTO;
import com.prgrms.ijuju.domain.chat.service.ChatService;
import com.prgrms.ijuju.global.auth.SecurityUser;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat/message")
    public void handleMessage(
            @Payload ChatMessageRequestDTO request,
            @Header("simpUser") SecurityUser user) {
        
        ChatMessageResponseDTO response = chatService.sendMessage(
            request.getRoomId(),
            user.getId(),
            request.getContent(),
            request.getImage()
        );

        messagingTemplate.convertAndSend("/topic/chat/room/" + request.getRoomId(), response);
    }

    @MessageMapping("/chat/read")
    public void handleReadMessage(
            @Payload ChatReadRequestDTO request,
            @Header("simpUser") SecurityUser user) {
        
        ChatReadResponseDTO response = chatService.markMessagesAsRead(
            request.getRoomId(), 
            user.getId()
        );
        
        messagingTemplate.convertAndSend("/topic/chat/room/" + request.getRoomId(), response);
    }
}
