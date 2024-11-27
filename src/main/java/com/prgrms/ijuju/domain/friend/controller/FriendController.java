package com.prgrms.ijuju.domain.friend.controller;

import com.prgrms.ijuju.domain.friend.dto.request.FriendRequestDTO;
import com.prgrms.ijuju.domain.friend.dto.response.UserResponseDTO;
import com.prgrms.ijuju.domain.friend.entity.FriendRequest;
import com.prgrms.ijuju.domain.friend.service.FriendService;
import com.prgrms.ijuju.domain.member.dto.request.MemberRequestDTO;
import com.prgrms.ijuju.domain.member.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @Autowired
    private MemberService memberService;

    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDTO>> searchUsers(@RequestParam String username) {
        List<UserResponseDTO> users = friendService.searchUsersByUsername(username);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/request")
    public ResponseEntity<Void> sendFriendRequest(@RequestBody FriendRequestDTO friendRequestDto) {
        if (friendRequestDto.getSenderId() == null || friendRequestDto.getReceiverId() == null) {
            return ResponseEntity.badRequest().build();
        }
        friendService.sendFriendRequest(friendRequestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/request/accept/{requestId}")
    public ResponseEntity<Void> acceptFriendRequest(@PathVariable Long requestId) {
        friendService.acceptFriendRequest(requestId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/request/reject/{requestId}")
    public ResponseEntity<Void> rejectFriendRequest(@PathVariable Long requestId) {
        friendService.rejectFriendRequest(requestId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/requests/received/{receiverId}")
    public ResponseEntity<List<FriendRequest>> getReceivedFriendRequests(@PathVariable Long receiverId) {
        List<FriendRequest> requests = friendService.getReceivedFriendRequests(receiverId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{memberId}/friends")
    public ResponseEntity<List<UserResponseDTO>> getFriends(@PathVariable Long memberId) {
        List<UserResponseDTO> friends = friendService.getFriends(memberId);
        return ResponseEntity.ok(friends);
    }

    @DeleteMapping("/{memberId}/friends/{friendId}")
    public ResponseEntity<Void> removeFriend(@PathVariable Long memberId, @PathVariable Long friendId) {
        friendService.removeFriend(memberId, friendId);
        return ResponseEntity.ok().build();
    }
} 
