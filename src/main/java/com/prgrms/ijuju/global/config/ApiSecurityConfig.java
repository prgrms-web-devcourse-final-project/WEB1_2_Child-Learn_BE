package com.prgrms.ijuju.global.config;

import com.prgrms.ijuju.domain.member.service.OAuth2Service;
import com.prgrms.ijuju.global.auth.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class ApiSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2Service oAuth2Service;

    @Bean
    SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        log.info("security 실행 ");
        http
                .oauth2Login(oauth2 -> oauth2
                        //.loginPage("/login") // 로그인 페이지 설정
                        .defaultSuccessUrl("/ouath/loginInfo", true)   // 로그인 성공 후 리다이렉트 경로
                        //.failureUrl("/login?error=true") // 로그인 실패 시 리다이렉트 경로
                        .userInfoEndpoint(userInfo ->
                                userInfo.userService(oAuth2Service)) // OAuth2 로그인 후 사용자 정보 처리
                );
        http
                .cors(cors -> cors.configure(http))  // CORS 설정 추가
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers ->
                        headers.frameOptions(frameOptions ->
                                frameOptions.disable()
                        )
                )
                .authorizeHttpRequests(auth -> auth
                        // 정적 리소스 허용
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/resources/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/login", "/oauth2/authorization/kakao", "/oauth2/authorization/google").permitAll()

                        .requestMatchers("/api/v1/advanced-invest/**").permitAll()
                        .requestMatchers("/ws/**").permitAll()
                        .requestMatchers("/ws-stomp/**").permitAll()

                        // API 엔드포인트 허용
                        .requestMatchers(
                            "/api/v1/member/join",
                            "/api/v1/member/login",
                            "/api/v1/member/refresh",
                            "/api/v1/member/reset-pw",
                            "/api/v1/member/find-id",
                            "/api/v1/member/check-id",
                            "/api/v1/member/logout")
                        .permitAll() // 로그인과 토큰 갱신 허용

                        .anyRequest().authenticated()   // 나머지는 인증 필요
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        log.info("JWT 필터 통과");

        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
