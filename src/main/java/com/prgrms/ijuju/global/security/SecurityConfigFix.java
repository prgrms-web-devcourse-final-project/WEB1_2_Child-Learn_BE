//package com.prgrms.ijuju.global.security;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//@RequiredArgsConstructor
//@Slf4j
//public class SecurityConfigFix {
//
//    @Bean
//    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        log.info("security 실행 ");
//        http
//                .csrf(csrf -> csrf.disable())
//                .headers(headers ->
//                        headers.frameOptions(frameOptions ->
//                                        frameOptions.disable()
//                                )
//                )
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//                .authorizeHttpRequests(auth -> auth
//                        // 정적 리소스 허용
//                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
//                        .requestMatchers("/resources/**").permitAll()
//                        .requestMatchers("/h2-console/**").permitAll()
//                        // API 엔드포인트 허용
//                        .requestMatchers(
//                                "/api/v1/member/join",
//                                "/api/v1/member/login",
//                                "/api/v1/member/refresh",
//                                "/api/v1/member/reset-pw",
//                                "/api/v1/member/find-id"
//                        ).permitAll()
//                        // 나머지는 인증 필요
//                        .anyRequest().authenticated()
//                );
//        return http.build();
//    }
//
////    @Bean
////    public PasswordEncoder passwordEncoder() {
////        return new BCryptPasswordEncoder();
////    }
//}
