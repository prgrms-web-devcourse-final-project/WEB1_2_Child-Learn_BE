package com.prgrms.ijuju.domain.member.controller;

import com.prgrms.ijuju.domain.member.dto.request.MemberRequestDTO;
import com.prgrms.ijuju.domain.member.dto.response.MemberResponseDTO;
import com.prgrms.ijuju.domain.member.service.MemberService;
import com.prgrms.ijuju.global.auth.SecurityUser;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
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

    // 아이디 중복 체크
    @GetMapping("/check-id")
    public ResponseEntity<Boolean> checkLoginId(@RequestParam String loginId) {
        boolean checkLoginId = memberService.checkLoginId(loginId);
        return ResponseEntity.ok(checkLoginId);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<MemberResponseDTO.LoginResponseDTO> login(
            @Validated @RequestBody MemberRequestDTO.LoginRequestDTO dto,
            HttpServletResponse response) {

        // 인증 성공
        MemberResponseDTO.LoginResponseDTO responseDTO = memberService.loginIdAndPw(dto.getLoginId(), dto.getPw(), response);

        log.info("인증 성공, 사용자 ID: {}, 로그인 ID: {}", responseDTO.getId(), responseDTO.getLoginId());
        log.info("Access Token: {}", responseDTO.getAccessToken());

        return ResponseEntity.ok(responseDTO);
    }

    // refresh Access Token 재발급
    @PostMapping("/refresh")
    public ResponseEntity<MemberResponseDTO.RefreshAccessTokenResponseDTO> loginAccessToken(@CookieValue("refreshToken") String refreshToken) {
        String accessToken = memberService.refreshAccessToken(refreshToken);
        LocalDateTime expiryAt = LocalDateTime.now().plusMinutes(15);
        MemberResponseDTO.RefreshAccessTokenResponseDTO responseDTO =
                new MemberResponseDTO.RefreshAccessTokenResponseDTO(accessToken, "새로운 Access Token 발급", expiryAt);

        return ResponseEntity.ok(responseDTO);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<MemberResponseDTO.LogoutResponseDTO> logout(@AuthenticationPrincipal SecurityUser user, HttpServletResponse response) {

        memberService.logout(user.getId(), response);

        return ResponseEntity.ok(new MemberResponseDTO.LogoutResponseDTO("로그아웃 되었습니다"));
    }

    // 나의 회원 정보 조회
    @GetMapping("/my-info")
    public ResponseEntity<MemberResponseDTO.ReadMyInfoResponseDTO> readMyInfo(@AuthenticationPrincipal SecurityUser user) {
        long id = user.getId();
        return ResponseEntity.ok(memberService.readMyInfo(id));
    }

    // 다른 회원 조회
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDTO.ReadOthersInfoResponseDTO> readOthersInfo(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.readOthersInfo(id));
    }

    // 모든 회원 목록 조회
    @GetMapping("/all")
    public ResponseEntity<Page<MemberResponseDTO.ReadAllResponseDTO>> readAll(
            @AuthenticationPrincipal SecurityUser user,
            MemberRequestDTO.PageRequestDTO dto) {
        Page<MemberResponseDTO.ReadAllResponseDTO> members = 
            memberService.readAll(dto, user.getId());
        return ResponseEntity.ok(members);
    }
    
    // username으로 회원 검색
    @GetMapping("/search")
    public ResponseEntity<Page<MemberResponseDTO.ReadAllResponseDTO>> searchMembers(
            @RequestParam String username,
            MemberRequestDTO.PageRequestDTO dto,
            @AuthenticationPrincipal SecurityUser user) {
        Page<MemberResponseDTO.ReadAllResponseDTO> searchResults = 
            memberService.searchByUsername(username, dto, user.getId());
        return ResponseEntity.ok(searchResults);
    }

    // 회원 탈퇴
    @PostMapping("/delete")
    public ResponseEntity<Map<String, String>> delete(@AuthenticationPrincipal SecurityUser user, @RequestBody MemberRequestDTO.DeleteRequestDTO dto) {
        // 비밀번호 확인
        long id = user.getId();
        memberService.delete(id, dto.getPw());
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
    @PostMapping("/find-id")
    public ResponseEntity<String> findId(@Validated @RequestBody MemberRequestDTO.FindLoginIdRequestDTO dto) {
        String loginId = memberService.findLoginIdByEmail(dto.getEmail(), dto.getBirth());

        return ResponseEntity.ok("가입하신 LoginId 는 " + loginId + " 입니다.");
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

    // 회원 활동 상태 확인
    @GetMapping("/active/{id}")
    public ResponseEntity<Map<String, Boolean>> checkMemberActiveStatus(@PathVariable Long id) {
        boolean isActive = memberService.checkMemberIsActive(id);
        return ResponseEntity.ok(Map.of("isActive", isActive));
    }

    // 회원 자신의 활성 상태 변경
    @PatchMapping("/my-active")
    public ResponseEntity<Map<String, String>> updateMyActiveStatus(
            @AuthenticationPrincipal SecurityUser user,
            @RequestParam boolean isActive) {
        memberService.updateMemberActiveStatus(user.getId(), isActive);
        String message = isActive ? "활성 상태로 변경되었습니다." : "비활성 상태로 변경되었습니다.";
        return ResponseEntity.ok(Map.of("message", message));
    }

    // ProfileImage 저장
    @PostMapping("/update-profile-image")
    public ResponseEntity<String> updateProfileImage(@RequestParam("profileImage") MultipartFile file,
                                                                                      @AuthenticationPrincipal SecurityUser user) throws IOException {
        Long id = user.getId();
        String profileImageUrl = memberService.updateProfileImage(id, file);
        return ResponseEntity.ok(profileImageUrl);
    }
}
