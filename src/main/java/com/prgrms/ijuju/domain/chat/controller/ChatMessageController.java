package com.prgrms.ijuju.domain.chat.controller;

import com.prgrms.ijuju.domain.chat.dto.request.ChatMessageRequestDTO;
import com.prgrms.ijuju.domain.chat.dto.request.ChatReadRequestDTO;
import com.prgrms.ijuju.domain.chat.dto.response.ChatMessageResponseDTO;
import com.prgrms.ijuju.domain.chat.dto.response.ChatReadResponseDTO;
import com.prgrms.ijuju.domain.chat.service.ChatService;
import com.prgrms.ijuju.domain.chat.service.ChatSessionService;
import com.prgrms.ijuju.global.auth.SecurityUser;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final ChatSessionService chatSessionService;

    // 채팅방 메시지 전송
    @MessageMapping("/chat/message")
    public void handleChatMessage(
            @Payload ChatMessageRequestDTO messageRequest,
            @Header("simpUser") SecurityUser user) {
        
        ChatMessageResponseDTO response = chatService.sendMessage(
            messageRequest.getRoomId(),
            user.getId(),
            messageRequest.getContent(),
            messageRequest.getImage()
        );
        
        messagingTemplate.convertAndSendToUser(
            messageRequest.getRoomId(),
            "/queue/messages",
            response
        );
    }

    // 채팅방 메시지 조회 - 페이지네이션 적용
    @MessageMapping("/chat/messages")
    public void handleShowMessages(
            @Payload Map<String, Object> request,
            @Header("simpUser") SecurityUser user) {
            
        String roomId = (String) request.get("roomId");
        String lastMessageId = (String) request.get("lastMessageId");
        int size = request.get("size") != null ? 
                  (int) request.get("size") : 20;
            
        Page<ChatMessageResponseDTO> messages = 
            chatService.showMessagesByScroll(roomId, lastMessageId, size, user.getId());
        
        messagingTemplate.convertAndSendToUser(
            user.getUsername(),
            "/queue/chat/messages/" + roomId,
            messages
        );
    }

    // 메시지 삭제
    @MessageMapping("/chat/message/delete")
    public void handleDeleteMessage(
            @Payload String messageId,
            @Header("simpUser") SecurityUser user) {
        
        chatService.deleteMessage(messageId, user.getId());
        messagingTemplate.convertAndSend("/topic/chat/message/delete/" + messageId, messageId);
    }

    // 채팅방 메시지 읽음 처리
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

    // 읽지 않은 메시지 수 조회
    @MessageMapping("/chat/unread")
    public void handleUnreadCount(
            @Header("simpUser") SecurityUser user) {
        
        int unreadCount = chatService.showUnreadCount(user.getId());
        messagingTemplate.convertAndSendToUser(
            user.getUsername(),
            "/queue/chat/unread",
            unreadCount
        );
    }

    // 하트비트 체크
    @MessageMapping("/chat/heartbeat")
    public void handleHeartbeat(@Header("simpUser") SecurityUser user) {
        chatSessionService.heartbeat(user.getId());
    }
}
