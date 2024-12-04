package com.prgrms.ijuju.domain.member.oAuth2;

public interface OAuth2Response {

    String getProvider();

    String getProviderId();

    String getEmail();

    String getNickName();

    String getProfileImage();
}
