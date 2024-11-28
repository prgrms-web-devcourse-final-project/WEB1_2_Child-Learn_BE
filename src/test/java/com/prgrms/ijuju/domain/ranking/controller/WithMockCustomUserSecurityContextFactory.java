package com.prgrms.ijuju.domain.ranking.controller;

import com.prgrms.ijuju.global.auth.SecurityUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Set<GrantedAuthority> authorities = Arrays.stream(customUser.roles())
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());

        SecurityUser principal = new SecurityUser(
                customUser.id(),
                customUser.username(),
                "password",
                authorities
        );

        Authentication auth = new UsernamePasswordAuthenticationToken(principal, "password", authorities);
        context.setAuthentication(auth);
        return context;
    }
}
