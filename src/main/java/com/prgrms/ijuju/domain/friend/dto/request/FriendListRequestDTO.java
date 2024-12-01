package com.prgrms.ijuju.domain.friend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FriendListRequestDTO {

    private Long memberId;
    private Integer page = 0;
    private Integer size = 10;
    private String searchKeyword;
    private Boolean isActive;
} 
