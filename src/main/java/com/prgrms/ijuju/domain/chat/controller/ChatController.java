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
import com.prgrms.ijuju.global.common.dto.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<ChatRoomListResponseDTO>>> getChatRoomList(
            @AuthenticationPrincipal SecurityUser user) {
        List<ChatRoomListResponseDTO> chatRooms = chatService.getChatRoomList(user.getId());
        return ResponseEntity.ok(ApiResponse.success("채팅방 목록을 조회했습니다.", chatRooms));
    }

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<ApiResponse<List<ChatMessageResponseDTO>>> getMessagesByChatRoomId(
            @PathVariable Long roomId,
            @AuthenticationPrincipal SecurityUser user) {
        List<ChatMessageResponseDTO> messages = chatService.getMessagesByChatRoomId(roomId, user.getId());
        return ResponseEntity.ok(ApiResponse.success("채팅 메시지를 조회했습니다.", messages));
    }

    @PostMapping("/rooms")
    public ResponseEntity<ApiResponse<ChatRoom>> createChatRoom(
            @AuthenticationPrincipal SecurityUser user,
            @RequestBody ChatRoomRequestDTO request) {
        ChatRoom chatRoom = chatService.createOrRestoreChatRoom(user.getId(), request.getFriendId());
        return ResponseEntity.ok(ApiResponse.success("채팅방이 생성되었습니다.", chatRoom));
    }

    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<ApiResponse<Void>> deleteChatRoom(
            @AuthenticationPrincipal SecurityUser user,
            @PathVariable Long roomId) {
        chatService.deleteChatRoom(roomId, user.getId());
        return ResponseEntity.ok(ApiResponse.success("채팅방이 삭제되었습니다."));
    }
}
