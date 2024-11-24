package com.prgrms.ijuju.domain.member.controller;

import com.prgrms.ijuju.domain.member.dto.request.MemberRequestDTO;
import com.prgrms.ijuju.domain.member.dto.response.MemberResponseDTO;
import com.prgrms.ijuju.domain.member.service.MemberService;
import com.prgrms.ijuju.global.security.SecurityUser;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/member")
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final JavaMailSender mailSender;

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
    public ResponseEntity<MemberResponseDTO.LogoutResponseDTO> logout(@AuthenticationPrincipal SecurityUser user) {
        memberService.setRefreshToken(user.getId(), "null");

        return ResponseEntity.ok(new MemberResponseDTO.LogoutResponseDTO("로그아웃 되었습니다"));
    }

    // 나의 회원 정보 조회
    @GetMapping("/my-info")
    public ResponseEntity<MemberResponseDTO.ReadMyInfoResponseDTO> readMyInfo(@AuthenticationPrincipal SecurityUser user) {
        long id = user.getId();
        return ResponseEntity.ok(memberService.readMyInfo(id)); // read 메서드 더블체크
    }

    // 다른 회원 조회
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDTO.ReadOthersInfoResponseDTO> readOthersInfo(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.readOthersInfo(id));
    }

    // 모든 회원 목록 조회
    @GetMapping("/all")
    public ResponseEntity<Page<MemberResponseDTO.ReadAllResponseDTO>> readAll(MemberRequestDTO.PageRequestDTO dto) {
        Page<MemberResponseDTO.ReadAllResponseDTO> readAllDTO = memberService.readAll(dto);
        return ResponseEntity.ok(readAllDTO);
    }

    // 회원 탈퇴
    @DeleteMapping("/")
    public ResponseEntity<Map<String, String>> delete(@AuthenticationPrincipal SecurityUser user) {
        long id = user.getId();
        memberService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    // 회원 정보 수정
    @PatchMapping("/my-info")
    public ResponseEntity<MemberResponseDTO.UpdateMyInfoResponseDTO> updateMyInfo(@AuthenticationPrincipal SecurityUser user,
                                                                                  @Validated @RequestBody MemberRequestDTO.UpdateMyInfoRequestDTO dto,
                                                                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 오류 메세지 생성
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining(", "));

            // 오류 응답 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MemberResponseDTO.UpdateMyInfoResponseDTO(errorMessage));
        }
        // 부분 업데이트 로직 처리
        long id = user.getId();
        dto.setId(id);
        MemberResponseDTO.UpdateMyInfoResponseDTO updateDTO = memberService.update(dto);
        return ResponseEntity.ok(memberService.update(dto));
    }

    // 아이디 찾기
    @GetMapping("/find-id")
    public ResponseEntity<String> findId(@RequestParam String email) {
        String loginId = memberService.findLoginIdByEmail(email);

        return ResponseEntity.ok(loginId);
    }

    // 비밀번호 재설정
    @PostMapping("/reset-pw")
    public ResponseEntity<String> resetPw(@RequestBody MemberRequestDTO.ResetPwRequestDTO dto) {
        String templatePw = memberService.resetPw(dto.getLoginId(), dto.getEmail());

        String title = "[아이주주] 비밀번호 재설정 인증 이메일입니다";
        String from = "subin8350@gmail.com";
        String to = dto.getEmail();
        String content = System.getProperty("line.separator") +
                System.getProperty("line.separator") +
                "임시 비밀번호로 로그인 후 꼭 새로운 비밀번호로 설정해주시기 바랍니다." +
                System.getProperty("line.separator") +
                System.getProperty("line.separator") +
                "임시 비밀번호는 " + templatePw + " 입니다. " +
                System.getProperty("line.separator");

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

            messageHelper.setFrom(from);
            messageHelper.setTo(to);
            messageHelper.setSubject(title);
            messageHelper.setText(content);

            mailSender.send(message);

        } catch (Exception e) {
            log.debug("임시 비밀번호 전송 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("임시 비밀번호 전송을 실패했습니다");
        }
        return ResponseEntity.ok("임시 비밀번호를 이메일로 전송했습니다");

    }

}
