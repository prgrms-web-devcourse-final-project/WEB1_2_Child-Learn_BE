//package com.prgrms.ijuju.global.security;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//@Configuration
//@EnableMethodSecurity
//@RequiredArgsConstructor
//@Slf4j
//public class SecurityConfig {
//
//    @Bean
//    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        log.info("security 실행 ");
//        http
//                .authorizeHttpRequests(
//                        authorizeHttpRequests -> authorizeHttpRequests
//                                .requestMatchers(
//                                        PathRequest.toStaticResources().atCommonLocations(),
//                                        new AntPathRequestMatcher("/resources/**"), // 정적 리소스 허용
//                                        new AntPathRequestMatcher("/h2-console/**") // H2 콘솔 접근 허용
////                                        new AntPathRequestMatcher("/api/v1/member/join"), // 회원가입 경로 추가
////                                        new AntPathRequestMatcher("/api/v1/member/login") // 로그인 경로 추가 (비인증 허용)
//                                )
//                                .permitAll()
////                                .requestMatchers(
////                                        "/adm/**"
////                                )
////                                .hasRole("ADMIN")
//                                .anyRequest().permitAll()
//                )
//                .headers(
//                        headers -> headers
//                                .addHeaderWriter(
//                                        new XFrameOptionsHeaderWriter(
//                                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN) // H2 Console을 iframe으로 로드할 수 있도록 SAMEORIGIN 설정
//                                )
//                )
//                //.csrf(csrf -> csrf.disable())
////                        (
////                        csrf -> csrf
////                                .ignoringRequestMatchers("/h2-console/**", "/api/v1/member/join") // H2 Console에 대해 CSRF 비활성화
////                                .disable()  // 개발용으로 CSRF 를 완전히 비활성화
////
////                )
//                .csrf(
//                        csrf -> csrf
//                                .ignoringRequestMatchers(
//                                        "/h2-console/**"
//                                )
//                )
//                //.formLogin(form -> form.disable());
//                //.httpBasic(Customizer.withDefaults()); // 기본 로그인 폼 설정
//                .formLogin(Customizer.withDefaults()); // 기본 로그인 폼 설정
//                //.httpBasic(Customizer.withDefaults()); // HTTP 기본 인증 사용
//
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//}
