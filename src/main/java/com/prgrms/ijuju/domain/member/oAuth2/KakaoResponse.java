package com.prgrms.ijuju.domain.member.oAuth2;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class KakaoResponse implements OAuth2Response {

    private final Map<String, Object> attribute;

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attribute.get("kakao_account");
        return kakaoAccount.get("email").toString();
    }

    @Override
    public String getUserName() {
        Map<String, Object> properties = (Map<String, Object>) attribute.get("properties");
        return properties.get("username").toString();
    }

    @Override
    public String getProfileImage() {
        Map<String, Object> properties = (Map<String, Object>) attribute.get("properties");
        return properties.get("profile_image").toString();
    }
}
