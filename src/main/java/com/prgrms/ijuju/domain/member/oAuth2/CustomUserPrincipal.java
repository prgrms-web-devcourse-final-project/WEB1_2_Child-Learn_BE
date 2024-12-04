package com.prgrms.ijuju.domain.member.oAuth2;

import com.prgrms.ijuju.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class CustomUserPrincipal implements UserDetails {

    private final String email;
    private final String role;

    public CustomUserPrincipal(Member member) {
        this.email = member.getEmail().toString();
        this.role = member.getRole().toString();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE" + this.role));
    }

    @Override
    public String getPassword(){
        return "";
    }

    @Override
    public String getUsername() {
        return email;
    }




}
