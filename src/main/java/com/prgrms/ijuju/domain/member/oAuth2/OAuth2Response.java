package com.prgrms.ijuju.domain.member.oAuth2;

public interface OAuth2Response {

    // 제공자 (google, kakao)
    String getProvider();

    // 제공자에서 발급해주는 아이디
    String getProviderId();

    String getEmail();

    String getUserName();

    String getProfileImage();
}
