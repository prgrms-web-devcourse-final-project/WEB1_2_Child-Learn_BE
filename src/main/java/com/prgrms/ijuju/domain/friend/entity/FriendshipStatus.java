package com.prgrms.ijuju.domain.friend.entity;

public enum FriendshipStatus {
    FRIEND("친구"),
    PENDING("친구 요청 중"),
    NOT_FRIEND("친구 아님");

    private final String description;

    FriendshipStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
} 