package com.prgrms.ijuju.global.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//@Configuration
//@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class ApiSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .securityMatcher("/api/**")
                .authorizeHttpRequests(auth -> auth

                            .requestMatchers("/api/v1/member/join",
                            "/api/v1/member/login",
                            "api/**",
                            "/api/v1/member/refresh").permitAll() // 로그인과 토큰 갱신 허용
//                .securityMatcher("/api/**")
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(authrize -> authrize
//
//                        .requestMatchers("/api/adm/**")
//                        .hasRole("ADMIN")
//
//                        // 인증 없이 접근 가능한 엔드포인트
//                        .requestMatchers(
//                                "/api/v1/member/login",
//                                "/api/v1/member/join",
//                                "/api/v1/member/refreshAccessToken",
//                                "api/**",
//                                "/api/v1/member/find/id",
//                                "/api/v1/member/find/pw"
//                        ).permitAll()
                        .anyRequest().authenticated()   // 나머지는 인증 필요
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        log.info("JWT 필터 통과");

        return http.build();

    }
}
