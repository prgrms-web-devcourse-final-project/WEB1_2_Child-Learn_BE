package com.prgrms.ijuju.domain.friend.dto.request;

import com.prgrms.ijuju.domain.friend.entity.RequestStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestStatusDTO {

    private RequestStatus status;
} 
