package com.prgrms.ijuju.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.prgrms.ijuju.domain.chat.dto.request.ChatRoomRequestDTO;
import com.prgrms.ijuju.domain.chat.dto.response.ChatRoomListResponseDTO;
import com.prgrms.ijuju.domain.chat.dto.response.ChatMessageResponseDTO;
import com.prgrms.ijuju.domain.chat.dto.response.UnreadCountResponseDTO;
import com.prgrms.ijuju.domain.chat.service.ChatService;
import com.prgrms.ijuju.global.auth.SecurityUser;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/rooms")
    public ResponseEntity<Map<String, Object>> createChatRoom(
            @AuthenticationPrincipal SecurityUser user,
            @RequestBody ChatRoomRequestDTO request) {
        ChatRoomListResponseDTO room = chatService.createOrGetChatRoom(user.getId(), request.getFriendId());
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "채팅방이 성공적으로 생성되었습니다.");
        response.put("data", room);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rooms")
    public ResponseEntity<Map<String, Object>> getChatRooms(
            @AuthenticationPrincipal SecurityUser user) {
        List<ChatRoomListResponseDTO> rooms = chatService.getChatRooms(user.getId());
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "채팅방 목록을 성공적으로 조회했습니다.");
        response.put("data", rooms);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<List<ChatMessageResponseDTO>> getChatMessages(
            @AuthenticationPrincipal SecurityUser user,
            @PathVariable String roomId) {
        List<ChatMessageResponseDTO> messages = chatService.getChatMessages(roomId, user.getId());
        return ResponseEntity.ok(messages);
    }

    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<Map<String, String>> deleteChatRoom(
            @AuthenticationPrincipal SecurityUser user,
            @PathVariable String roomId) {
        chatService.deleteChatRoom(roomId, user.getId());
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "채팅방이 성공적으로 삭제되었습니다.");
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Void> deleteMessage(
            @AuthenticationPrincipal SecurityUser user,
            @PathVariable String messageId) {
        chatService.deleteMessage(messageId, user.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unread")
    public ResponseEntity<UnreadCountResponseDTO> getUnreadCount(
            @AuthenticationPrincipal SecurityUser user) {
        int count = chatService.getUnreadCount(user.getId());
        return ResponseEntity.ok(new UnreadCountResponseDTO(count));
    }
}
