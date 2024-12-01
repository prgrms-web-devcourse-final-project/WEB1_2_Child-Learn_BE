package com.prgrms.ijuju.domain.chat.controller;

import com.prgrms.ijuju.domain.chat.dto.request.ChatMessageRequestDTO;
import com.prgrms.ijuju.domain.chat.dto.request.ChatReadRequestDTO;
import com.prgrms.ijuju.domain.chat.dto.response.ChatMessageResponseDTO;
import com.prgrms.ijuju.domain.chat.dto.response.ChatReadResponseDTO;
import com.prgrms.ijuju.domain.chat.dto.response.UnreadCountResponseDTO;
import com.prgrms.ijuju.domain.chat.service.ChatService;
import com.prgrms.ijuju.global.auth.SecurityUser;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/chats")
public class ChatMessageController {
    
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    // 메시지 전송
    @MessageMapping("/send")
    public void sendMessage(@Payload ChatMessageRequestDTO message, 
                          @AuthenticationPrincipal SecurityUser user) {
        ChatMessageResponseDTO response = chatService.saveMessage(message, user.getId());
        messagingTemplate.convertAndSend("/topic/chat/room/" + message.getRoomId(), response);
        updateUnreadCount(message.getRoomId(), user.getId());
    }

    // 메시지 읽음 처리
    @MessageMapping("/read")
    public void readMessage(@Payload ChatReadRequestDTO request, 
                          @AuthenticationPrincipal SecurityUser user) {
        chatService.markAsRead(request.getRoomId(), user.getId());
        ChatReadResponseDTO response = new ChatReadResponseDTO(user.getId(), request.getRoomId());
        messagingTemplate.convertAndSend("/topic/chat/room/" + request.getRoomId() + "/read", response);
    }

    // 메시지 삭제
    @DeleteMapping("/send/{messageId}")
    public ResponseEntity<Void> deleteMessage(
        @AuthenticationPrincipal SecurityUser user,
        @PathVariable Long messageId
    ) {
        chatService.deleteMessage(messageId, user.getId());
        return ResponseEntity.ok().build();
    }
        
    // 읽지 않은 메시지 개수 업데이트
    private void updateUnreadCount(Long roomId, Long userId) {
        int unreadCount = chatService.getUnreadMessageCount(roomId, userId);
        messagingTemplate.convertAndSend("/topic/chat/room/" + roomId + "/unread", 
            new UnreadCountResponseDTO(unreadCount));
    }
}
