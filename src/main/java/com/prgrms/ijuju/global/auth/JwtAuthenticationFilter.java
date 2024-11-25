package com.prgrms.ijuju.global.auth;

import com.prgrms.ijuju.global.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // 각 요청마다 JWT 토큰을 확인하고 인증 정보를 설정

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String bearerToken = request.getHeader("Authorization");
        // HTTP 요청 헤더에서 "Authorization" 값을 가져옴

        if (bearerToken != null) {
            try {
                String token = bearerToken.substring("Bearer ".length());

                // 위변조 체크 및 디코드
                // JwtUtil 클래스의 decode() 메서드를 사용해 JWT 토큰을 디코딩하고 서명 위변조가 없는지 검사
                Claims claims = JwtUtil.decode(token);

                //claim의 정보들 파싱
                // JWT 토큰의 Claims에서 "data" 라는 키를 통해 데이터 맵을 얻어옴 => 여기에는 사용자의 상세 정보 포함
                Map<String, Object> data = (Map<String, Object>) claims.get("data");

                // 사용자 ID 추출 : 맵에서 "id"키에 해당하는 값을 추출 => 문자열 형태로 저장되어있기 때문에 Long 타입으로 변환해야함
                long id = Long.parseLong((String) data.get("id"));
                String loginId = (String) data.get("loginId");

                // 사용자의 권한 정보(문자열)를 추출, GrantedAuthority 객체로 변환
                List<? extends GrantedAuthority> authorities = ((List<String>) data.get("authorities"))
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                // Jwt에서 추출한 사용자 정보로 SecurityUser 객체를 생성
                SecurityUser user = new SecurityUser(id, loginId, "", authorities);

                //SecurityContext에 넣을  Authenticiation 생성
                Authentication auth = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());

                //SecurityContext에 auth 넣기
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
                log.debug("유효하지 않은 JWT 토큰 : {}", e.getMessage());
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "유효하지 않은 토큰입니다.");
                return;
            } catch (ExpiredJwtException e) {
                log.debug("만료된 JWT 토큰 : {}", e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access 토큰이 만료되었습니다.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
