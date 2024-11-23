package com.prgrms.ijuju.global.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class SecurityUser extends User {

    @Getter
    private long id;
    private String loginId;
    private Set<GrantedAuthority> authorities;

    public SecurityUser(long id , String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
        this.loginId = username;
        this.authorities = authorities.stream().collect(Collectors.toSet());
    }
}
