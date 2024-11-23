package com.prgrms.ijuju.domain.member.controller;

import com.prgrms.ijuju.domain.member.dto.request.MemberRequestDTO;
import com.prgrms.ijuju.domain.member.dto.response.MemberResponseDTO;
import com.prgrms.ijuju.domain.member.service.MemberService;
import com.prgrms.ijuju.global.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/member")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<MemberResponseDTO.CreateResponseDTO> register(@Validated @RequestBody MemberRequestDTO.CreateRequestDTO request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 오류 메세지 생성
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining(", "));

            // 오류 응답 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MemberResponseDTO.CreateResponseDTO(errorMessage));
        }

        // 회원 생성 요청 처리
        MemberResponseDTO.CreateResponseDTO createResponseDTO = memberService.create(request);

        // 성공 응답 반환
        return ResponseEntity.ok(createResponseDTO);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<MemberResponseDTO.LoginResponseDTO> login(@Validated @RequestBody MemberRequestDTO.LoginRequestDTO dto) {
        // 인증 성공
        MemberResponseDTO.LoginResponseDTO responseDTO = memberService.loginIdAndPw(dto.getLoginId(), dto.getPw());

        Long id = responseDTO.getId();
        String loginId = responseDTO.getLoginId();

        log.info("인증 성공, 사용자 ID: {}, 로그인 ID: {}", id, loginId);

        String accessToken = memberService.generateAccessToken(id, loginId);
        String refreshToken = memberService.generateRefreshToken(id, loginId);

        log.info("Access Token 생성: {}", accessToken);
        log.info("Refresh Token 생성: {}", refreshToken);

        memberService.setRefreshToken(id, refreshToken);

        responseDTO.setAccessToken(accessToken);
        responseDTO.setRefreshToken(refreshToken);

        return ResponseEntity.ok(responseDTO);
    }

    // refresh Access Token 재발급
    @PostMapping("/refresh")
    public ResponseEntity<MemberResponseDTO.RefreshAccessTokenResponseDTO> loginAccessToken(@RequestBody MemberRequestDTO.RefreshAccessTokenRequestDTO dto) {
        String accessToken = memberService.refreshAccessToken(dto.getRefreshToken());
        MemberResponseDTO.RefreshAccessTokenResponseDTO responseDTO = new MemberResponseDTO.RefreshAccessTokenResponseDTO(accessToken, "새로운 Access Token 발급");

        return ResponseEntity.ok(responseDTO);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<MemberResponseDTO.logoutResponseDTO> logout(@AuthenticationPrincipal SecurityUser user) {
        memberService.setRefreshToken(user.getId(), "null");

        return ResponseEntity.ok(new MemberResponseDTO.logoutResponseDTO("로그아웃 되었습니다"));
    }

    // 나의 회원 정보 조회
    @GetMapping("/my-info")
    public ResponseEntity<MemberResponseDTO.readMyInfoResponseDTO> readMyInfo(@AuthenticationPrincipal SecurityUser user) {
        long id = user.getId();
        return ResponseEntity.ok(memberService.readMyInfo(id)); // read 메서드 더블체크
    }

    // 다른 회원 조회
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDTO.readOthersInfoResponseDTO> readOthersInfo(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.readOthersInfo(id));
    }

    // 회원 탈퇴

    // 회원 정보 수정
    @PatchMapping("/my-info")
    public ResponseEntity<MemberResponseDTO.updateMyInfoResponseDTO> updateMyInfo(@AuthenticationPrincipal SecurityUser user, @Validated @RequestBody MemberRequestDTO.updateMyInfoRequestDTO dto) {
        // 부분 업데이트 로직 처리
        long id = user.getId();
        dto.setId(id);
        return ResponseEntity.ok(memberService.update(dto));
    }

    // 아이디 찾기

    // 비밀번호 찾기

}
