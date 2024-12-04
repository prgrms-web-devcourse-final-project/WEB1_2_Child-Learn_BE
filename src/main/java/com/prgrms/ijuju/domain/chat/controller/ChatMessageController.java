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
public class ChatMessageController { // STOMP를 사용한 채팅 메시지 컨트롤러

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/message")
    public void message(@Payload ChatMessageRequestDTO message,
                       @Header("simpUser") SecurityUser user) {
        ChatMessageResponseDTO response = chatService.sendMessage(message, user.getId());
        
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), response);
    }

    @MessageMapping("/chat/read")
    public void readMessage(@Payload ChatReadRequestDTO request,
                          @Header("simpUser") SecurityUser user) {
        ChatReadResponseDTO response = chatService.markAsRead(request.getRoomId(), user.getId());
        
        messagingTemplate.convertAndSend("/sub/chat/room/" + request.getRoomId(), response);
    }
}
