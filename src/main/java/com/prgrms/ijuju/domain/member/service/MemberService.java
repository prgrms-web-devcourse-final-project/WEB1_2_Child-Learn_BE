package com.prgrms.ijuju.domain.member.service;

import com.prgrms.ijuju.domain.member.dto.request.MemberRequestDTO;
import com.prgrms.ijuju.domain.member.dto.response.MemberResponseDTO;
import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.exception.MemberException;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.global.util.JwtUtil;
import com.prgrms.ijuju.global.util.PasswordUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @Transactional
    public MemberResponseDTO.CreateResponseDTO create(MemberRequestDTO.CreateRequestDTO dto) {
        log.info("회원가입 요청 시작 : {} ", dto.getLoginId());
        try {
            // 입력한 loginId로 가입한 회원이 있는지 확인
            Optional<Member> member = memberRepository.findByLoginId(dto.getLoginId());
            if (member.isPresent()) {
                throw new RuntimeException("이미 존재하는 아이디입니다");
            }

            // 비밀번호 암호화 처리
            String encodePw = dto.getPw();
            dto.setPw(passwordEncoder.encode(encodePw));

            Member savedMember = memberRepository.save(dto.toEntity());
            return new MemberResponseDTO.CreateResponseDTO("회원가입이 완료되었습니다.");

        } catch (Exception e) {
            throw MemberException.MEMBER_NOT_REGISTERED.getMemberTaskException();
        }
    }

    // 로그인
    public MemberResponseDTO.LoginResponseDTO loginIdAndPw(String loginId, String pw) {
        Optional<Member> findMember = memberRepository.findByLoginId(loginId);
        if (findMember.isEmpty()) {
            throw MemberException.MEMBER_NOT_FOUND.getMemberTaskException();
        }

        if (!passwordEncoder.matches(pw, findMember.get().getPw())) {
            throw MemberException.MEMBER_LOGIN_DENIED.getMemberTaskException();
        }

        Member member = findMember.get();
        MemberResponseDTO.LoginResponseDTO responseDTO = new MemberResponseDTO.LoginResponseDTO(member);

        return responseDTO;
    }

    // Access Token 생성
    public String generateAccessToken(Long id, String loginId) {
        List<String> authorities;
        if (loginId.equals("admin")) {
            authorities = List.of("ROLE_ADMIN");
        } else {
            authorities = List.of("ROLE_MEMBER");
        }

        return JwtUtil.encodeAccessToken(15,
                Map.of("id", id.toString(),
                        "loginId", loginId,
                        "authorities", authorities)
        );
    }

    // Refresh Token 생성
    public String generateRefreshToken(Long id, String loginId) {
        return JwtUtil.encodeRefreshToken(60 * 24 * 3,
                Map.of("id", id.toString(),
                        "loginId", loginId)
        );
    }

    // refresh Access Token
    public String refreshAccessToken(String refreshToken) {
        // 화이트리스트 처리
        Member member = memberRepository.findByRefreshToken(refreshToken)
                .orElseThrow( () -> MemberException.MEMBER_LOGIN_DENIED.getMemberTaskException());

        // 리프레시 토큰이 만료되었다면 로그아웃
        try {
            Claims claims = JwtUtil.decode(refreshToken);   // 여기서 에러 처리가 남
        } catch (ExpiredJwtException e) {
            // 클라이언트한테 만료되었다고 알려주기
            throw MemberException.MEMBER_REFRESHTOKEN_EXPIRED.getMemberTaskException();
        }
        return generateAccessToken(member.getId(), member.getLoginId());
    }

    // setRefreshToken
    @Transactional
    public void setRefreshToken(Long id, String refreshToken) {
        Member member = memberRepository.findById(id).get();
        member.updateRefreshToken(refreshToken);
    }

    // -------------------------------------------------------------

    // 나의 회원 정보 조회
    public MemberResponseDTO.readMyInfoResponseDTO readMyInfo(Long id) {
        Optional<Member> opMember = memberRepository.findById(id);
        if (opMember.isPresent()) {
            Member member = opMember.get();
            return new MemberResponseDTO.readMyInfoResponseDTO(member);
        } else {
            throw MemberException.MEMBER_NOT_FOUND.getMemberTaskException();
        }
    }

    // 다른 회원 정보 조회
    public MemberResponseDTO.readOthersInfoResponseDTO readOthersInfo(Long id) {
        Optional<Member> opMember = memberRepository.findById(id);
        if (opMember.isPresent()) {
            Member member = opMember.get();
            return new MemberResponseDTO.readOthersInfoResponseDTO(member);
        } else {
            throw MemberException.MEMBER_NOT_FOUND.getMemberTaskException();
        }
    }

    // 회원 탈퇴
    @Transactional
    public void delete(Long id) {
        Optional<Member> opMember = memberRepository.findById(id);
        if (opMember.isPresent()) {
            Member member = opMember.get();
            memberRepository.delete(member);
        } else {
            throw MemberException.MEMBER_NOT_REMOVED.getMemberTaskException();
        }
    }

    // 회원 정보 수정
    @Transactional
    public MemberResponseDTO.updateMyInfoResponseDTO update(MemberRequestDTO.updateMyInfoRequestDTO dto) {
        Optional<Member> opMember = memberRepository.findById(dto.getId());

        if (opMember.isPresent()) {
            Member member = opMember.get();
            member.changePw(passwordEncoder.encode(dto.getPw()));
            member.changeUsername(dto.getUsername());
            memberRepository.save(member);

            return new MemberResponseDTO.updateMyInfoResponseDTO(
                    member.getId(),
                    member.getPw(),
                    member.getUsername()
            );
        } else {
            throw MemberException.MEMBER_NOT_MODIFIED.getMemberTaskException();
        }
    }

    // 아이디 찾기
    public String findLoginIdByEmail(String email) {
        Optional<Member> opMember = memberRepository.findByEmail(email);
        if (opMember.isPresent()) {
            String loginId = opMember.get().getLoginId();
            return maskLoginId(loginId);
        } else {
            throw MemberException.MEMBER_NOT_FOUND.getMemberTaskException();
        }
    }

    // 아이디 마스킹 처리....
    private String maskLoginId(String loginId) {

        int maskLength = loginId.length() - 2;
        StringBuilder masked = new StringBuilder();
        masked.append(loginId.substring(0, 2)); // 앞의 두 글자는 그대로 보여줌

        for (int i = 0; i < maskLength; i++) {
            masked.append("*");
        }

        return masked.toString();
    }

    // 비밀번호 찾기
    @Transactional
    public String setTemplatePw(String loginId, String email) {
        Optional<Member> opMember = memberRepository.findByLoginIdAndEmail(loginId, email);
        if (opMember.isPresent()) {
            Member member = opMember.get();
            String templatePw = PasswordUtil.generateTempPassword();
            member.changePw(templatePw);
            return templatePw;
        } else {
            throw MemberException.MEMBER_NOT_FOUND.getMemberTaskException();
        }
    }


}
