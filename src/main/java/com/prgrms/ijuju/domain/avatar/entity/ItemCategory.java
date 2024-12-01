package com.prgrms.ijuju.domain.avatar.entity;

public enum ItemCategory {
    BACKGROUND("background"),
    PET("pet"),
    HAT("hat");

    private final String displayName;

    ItemCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return displayName;
    }

}
