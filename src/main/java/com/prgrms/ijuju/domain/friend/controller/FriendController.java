package com.prgrms.ijuju.domain.friend.controller;

import com.prgrms.ijuju.domain.friend.dto.request.FriendRequestDto;
import com.prgrms.ijuju.domain.friend.dto.response.UserResponseDto;
import com.prgrms.ijuju.domain.friend.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendController {

    @Autowired
    private FriendService friendService;

    // 전체 사용자 목록 조회
    
    // 사용자 별명으로 검색

    // 친구 요청 보내기
    @PostMapping("/request")
    public ResponseEntity<Void> sendFriendRequest(@RequestBody FriendRequestDto friendRequestDto) {
        friendService.sendFriendRequest(friendRequestDto.getMemberId(), friendRequestDto.getFriendId());
        return ResponseEntity.ok().build();
    }

    // 친구 목록 조회
    @GetMapping("/{memberId}/friends")
    public ResponseEntity<List<UserResponseDto>> getFriends(@PathVariable Long memberId) {
        List<UserResponseDto> friends = friendService.getFriends(memberId);
        return ResponseEntity.ok(friends);
    }

    // 친구 요청 수락
    @PostMapping("/request/accept/{requestId}")
    public ResponseEntity<Void> acceptFriendRequest(@PathVariable Long requestId) {
        friendService.acceptFriendRequest(requestId);
        return ResponseEntity.ok().build();
    }

    // 친구 삭제
    @DeleteMapping("/{memberId}/friends/{friendId}")
    public ResponseEntity<Void> removeFriend(@PathVariable Long memberId, @PathVariable Long friendId) {
        friendService.removeFriend(memberId, friendId);
        return ResponseEntity.ok().build();
    }
} 