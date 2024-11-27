package com.prgrms.ijuju.domain.friend.dto.request;

import com.prgrms.ijuju.domain.friend.entity.FriendRequest;
import com.prgrms.ijuju.domain.friend.entity.RequestStatus;
import com.prgrms.ijuju.domain.member.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestDTO {
    private Long senderId;
    private Long receiverId;

    public FriendRequest toEntity() {
        return FriendRequest.builder()
                .sender(Member.builder().id(senderId).build())
                .receiver(Member.builder().id(receiverId).build())
                .status(RequestStatus.PENDING)
                .build();
    }
} 
