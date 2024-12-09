package com.prgrms.ijuju.domain.friend.dto.response;

import com.prgrms.ijuju.domain.friend.entity.FriendRequest;
import com.prgrms.ijuju.domain.friend.entity.RequestStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FriendResponseDTO {

    private Long id;
    private RequestStatus status;
    private boolean isRead;
    private String senderUsername;
    private String receiverUsername;

    public FriendResponseDTO(FriendRequest friendRequest) {
        this.id = friendRequest.getId();
        this.status = friendRequest.getRequestStatus();
        this.isRead = friendRequest.isRead();
        this.senderUsername = friendRequest.getSender().getUsername();
        this.receiverUsername = friendRequest.getReceiver().getUsername();
    }
} 
