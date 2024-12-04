package com.prgrms.ijuju.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.prgrms.ijuju.domain.chat.dto.request.ChatRoomRequestDTO;
import com.prgrms.ijuju.domain.chat.dto.response.ChatRoomListResponseDTO;
import com.prgrms.ijuju.domain.chat.dto.response.ChatMessageResponseDTO;
import com.prgrms.ijuju.domain.chat.entity.ChatRoom;
import com.prgrms.ijuju.domain.chat.service.ChatService;
import com.prgrms.ijuju.global.auth.SecurityUser;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chats")
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // 채팅방 목록 조회
    @GetMapping("/list")
    public ResponseEntity<List<ChatRoomListResponseDTO>> getChatRoomList(
        @AuthenticationPrincipal SecurityUser user
    ) {
        return ResponseEntity.ok(chatService.getChatRoomList(user.getId()));
    }

    // 채팅방(메시지) 조회
    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<List<ChatMessageResponseDTO>> getMessagesByChatRoomId(
        @PathVariable Long roomId,
        @AuthenticationPrincipal SecurityUser user
    ) {
        return ResponseEntity.ok(chatService.getMessagesByChatRoomId(roomId, user.getId()));
    }

    // 채팅방 생성
    @PostMapping("/rooms")
    public ResponseEntity<ChatRoom> createChatRoom(
        @AuthenticationPrincipal SecurityUser user,
        @RequestBody ChatRoomRequestDTO request
    ) {
        return ResponseEntity.ok(chatService.createOrRestoreChatRoom(user.getId(), request.getFriendId()));
    }

    // 채팅방 삭제
    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<String> deleteChatRoom(
        @AuthenticationPrincipal SecurityUser user,
        @PathVariable Long roomId
    ) {
        chatService.deleteChatRoom(roomId, user.getId());
        return ResponseEntity.ok("채팅방이 삭제되었습니다.");
    }
}

