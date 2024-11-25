package com.prgrms.ijuju.domain.friend.dto.response;

import com.prgrms.ijuju.domain.member.entity.Member;
import lombok.Getter;

@Getter
public class UserResponseDto { // 전체 사용자 정보 조회 응답 DTO
    private Long id;
    private String username;
    private String profileImage;
    private boolean isFriend;
    private boolean isActive;

   public UserResponseDto(Member member, boolean isFriend) {
       this.id = member.getId();
       this.username = member.getUsername();
       this.profileImage = member.getProfileImage();
       this.isFriend = isFriend;
       this.isActive = member.isActive();
   }
} 