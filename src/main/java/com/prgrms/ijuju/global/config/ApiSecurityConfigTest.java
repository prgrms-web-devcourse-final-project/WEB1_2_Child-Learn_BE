package com.prgrms.ijuju.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 테스트용으로 쓰는 Security config 입니다
 * Security 관련 기능을 사용하면서 기능 테스트를 하기 귀잖아 만들었습니다
 * Spring.profile.active = test 사용시 이걸로 대체되어 사용됩니다
 * 모든 Security 기능이 꺼집니다
 */

@Configuration
@Profile("test")
public class ApiSecurityConfigTest {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**")
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}