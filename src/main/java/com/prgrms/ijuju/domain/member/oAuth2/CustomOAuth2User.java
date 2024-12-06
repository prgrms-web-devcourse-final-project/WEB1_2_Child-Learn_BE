package com.prgrms.ijuju.domain.member.oAuth2;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final OAuth2Response oauth2Response;
    private final String role;
    private final Long memberId;

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of("mid",oauth2Response.getNickName(), "role", role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getName() {
        return oauth2Response.getNickName();
    }

    public Long getMemberId() {
        return memberId;
    }
}
