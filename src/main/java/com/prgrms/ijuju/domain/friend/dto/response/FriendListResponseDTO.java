package com.prgrms.ijuju.domain.friend.dto.response;

import com.prgrms.ijuju.domain.friend.entity.FriendList;
import com.prgrms.ijuju.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FriendListResponseDTO { // 친구 정보 담음

    private Long id;
    private String username;
    private String loginId;
    private String profileImage;
    private boolean isActive;

    public FriendListResponseDTO(FriendList friendList) {
        Member friend = friendList.getFriend();
        this.id = friend.getId();
        this.username = friend.getUsername();
        this.loginId = friend.getLoginId();
        this.profileImage = friend.getProfileImage();
        this.isActive = friend.isActive();
    }
} 
