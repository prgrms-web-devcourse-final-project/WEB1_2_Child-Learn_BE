package com.prgrms.ijuju.domain.friend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestDTO { // 친구 요청 DTO
    private Long memberId;
    private Long friendId;
} 
