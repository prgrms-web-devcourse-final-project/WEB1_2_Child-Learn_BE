package com.prgrms.ijuju.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.prgrms.ijuju.domain.chat.dto.request.ChatRoomRequestDTO;
import com.prgrms.ijuju.domain.chat.dto.response.ChatRoomListResponseDTO;
import com.prgrms.ijuju.domain.chat.service.ChatService;
import com.prgrms.ijuju.global.auth.SecurityUser;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;
    private final RedisTemplate<String, Object> redisTemplate;

    // 채팅방 생성
    @PostMapping("/rooms")
    public ResponseEntity<Map<String, Object>> createChatRoom(
            @AuthenticationPrincipal SecurityUser user,
            @RequestBody ChatRoomRequestDTO request) {
        ChatRoomListResponseDTO room = chatService.createChatRoom(user.getId(), request.getFriendId());
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "채팅방이 성공적으로 생성되었습니다.");
        response.put("data", room);
        
        return ResponseEntity.ok(response);
    }

    // 채팅방 목록 조회
    @GetMapping("/rooms")
    public ResponseEntity<Map<String, Object>> showChatRooms(
            @AuthenticationPrincipal SecurityUser user) {
        List<ChatRoomListResponseDTO> rooms = chatService.showChatRooms(user.getId());
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "채팅방 목록을 성공적으로 조회했습니다.");
        response.put("data", rooms);
        
        return ResponseEntity.ok(response);
    }
    
    // 채팅방 삭제
    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<Map<String, String>> deleteChatRoom(
            @AuthenticationPrincipal SecurityUser user,
            @PathVariable String roomId) {
        chatService.deleteChatRoom(roomId, user.getId());
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "채팅방이 성공적으로 삭제되었습니다.");
        
        return ResponseEntity.ok(response);
    }

    // Redis 연결 테스트
    @GetMapping("/redis-test")
    public ResponseEntity<String> testRedisConnection() {
        try {
            String testKey = "test:connection";
            redisTemplate.opsForValue().set(testKey, "테스트 성공");
            String result = (String) redisTemplate.opsForValue().get(testKey);
            redisTemplate.delete(testKey);
            
            return ResponseEntity.ok("Redis 연결 성공: " + result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Redis 연결 실패: " + e.getMessage());
        }
    }
}
