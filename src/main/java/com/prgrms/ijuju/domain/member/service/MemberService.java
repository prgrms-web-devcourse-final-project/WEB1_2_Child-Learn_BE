package com.prgrms.ijuju.domain.member.service;

import com.prgrms.ijuju.domain.avatar.entity.Avatar;
import com.prgrms.ijuju.domain.avatar.repository.AvatarRepository;
import com.prgrms.ijuju.domain.member.dto.request.MemberRequestDTO;
import com.prgrms.ijuju.domain.member.dto.response.MemberResponseDTO;
import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.exception.MemberException;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.wallet.entity.Wallet;
import com.prgrms.ijuju.global.util.JwtUtil;
import com.prgrms.ijuju.global.util.PasswordUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.prgrms.ijuju.domain.wallet.repository.WalletRepository;
import com.prgrms.ijuju.domain.wallet.exception.WalletException;
import com.prgrms.ijuju.domain.friend.service.FriendService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final WalletRepository walletRepository;
    private final AvatarRepository avatarRepository;
    private final FriendService friendService;

    // 회원가입
    @Transactional
    public MemberResponseDTO.CreateResponseDTO create(MemberRequestDTO.CreateRequestDTO dto) {
        log.info("회원가입 요청 시작 : {} ", dto.getLoginId());
        try {
            // 입력한 loginId로 가입한 회원이 있는지 확인
            log.info("아이디 중복 확인 : {}", dto.getLoginId());
            if (checkLoginId(dto.getLoginId())) {
                log.error("이미 존재하는 아이디 : {}", dto.getLoginId());
                throw MemberException.LOGINID_IS_DUPLICATED.getMemberTaskException();
            }

            // 비밀번호 암호화 처리
            String encodePw = dto.getPw();
            dto.setPw(passwordEncoder.encode(encodePw));

            // 회원 저장
            Member savedMember = memberRepository.save(dto.toEntity());

            // Wallet 생성 및 초기화
            Wallet wallet = Wallet.builder()
                .member(savedMember)
                .currentCoins(0L)
                .currentPoints(0L)
                .build();
            walletRepository.save(wallet);
            
            // 아바타 생성
            Avatar newAvatar = Avatar.builder()
                    .member(savedMember)
                    .background(null)
                    .pet(null)
                    .hat(null)
                    .build();

            avatarRepository.save(newAvatar);

            return new MemberResponseDTO.CreateResponseDTO("회원가입이 완료되었습니다.");
        } catch (Exception e) {
            throw MemberException.MEMBER_NOT_REGISTERED.getMemberTaskException();
        }
    }

    // 회원가입 시 같은 아이디 검증 메서드
    public boolean checkLoginId(String loginId) {
        log.info("아이디 중복 체크 : {}", loginId);
        return memberRepository.findByLoginId(loginId).isPresent();
    }

    // 로그인
    public MemberResponseDTO.LoginResponseDTO loginIdAndPw(String loginId, String pw, HttpServletResponse response) {
        // 동시 로그인 검증
        validateActiveStatus(loginId);
        
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> MemberException.MEMBER_NOT_FOUND.getMemberTaskException());

        if (!passwordEncoder.matches(pw, member.getPw())) {
            throw MemberException.MEMBER_LOGIN_DENIED.getMemberTaskException();
        }

        // 로그인 시 활성 상태로 변경
        member.updateActiveStatus(true);
        memberRepository.save(member);

        String accessToken = generateAccessToken(member.getId(), loginId);
        String refreshToken = generateRefreshToken(member.getId(), loginId);

        addRefreshTokenToCookie(refreshToken, response);

        LocalDateTime expiryDate = LocalDateTime.now().plusDays(3);
        setRefreshToken(member.getId(), refreshToken, expiryDate);

        MemberResponseDTO.LoginResponseDTO responseDTO = new MemberResponseDTO.LoginResponseDTO(member);
        responseDTO.setAccessToken(accessToken);

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

    // 리프레시 토큰 쿠키에 담기
    public void addRefreshTokenToCookie(String refreshToken, HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true); // javaScript에서 접근 불가
        refreshTokenCookie.setSecure(false);
        refreshTokenCookie.setPath("/");    // 모든 경로에서 유효
        refreshTokenCookie.setMaxAge(3 * 24 * 60 * 60); // 3일간 유효

        response.addCookie(refreshTokenCookie);
    }

    // 리프레시 토큰 저장
    @Transactional
    public void setRefreshToken(Long id, String refreshToken, LocalDateTime expiryDate) {
        Member member = memberRepository.findById(id).get();
        member.updateRefreshToken(refreshToken, expiryDate);
    }

    // 나의 회원 정보 조회
    public MemberResponseDTO.ReadMyInfoResponseDTO readMyInfo(Long id) {
        Optional<Member> opMember = memberRepository.findById(id);
        if (opMember.isPresent()) {
            Member member = opMember.get();
            Wallet wallet = walletRepository.findByMemberId(member.getId())
                    .orElseThrow(() -> WalletException.WALLET_NOT_FOUND.toException());
            return new MemberResponseDTO.ReadMyInfoResponseDTO(member, wallet);
        } else {
            throw MemberException.MEMBER_NOT_FOUND.getMemberTaskException();
        }
    }

    // 다른 회원 정보 조회
    public MemberResponseDTO.ReadOthersInfoResponseDTO readOthersInfo(Long id) {
        Optional<Member> opMember = memberRepository.findById(id);
        if (opMember.isPresent()) {
            Member member = opMember.get();
            Wallet wallet = walletRepository.findByMemberId(member.getId())
                    .orElseThrow(() -> WalletException.WALLET_NOT_FOUND.toException());
            return new MemberResponseDTO.ReadOthersInfoResponseDTO(member, wallet);
        } else {
            throw MemberException.MEMBER_NOT_FOUND.getMemberTaskException();
        }
    }

    // 전체 회원 목록
    public Page<MemberResponseDTO.ReadAllResponseDTO> readAll(MemberRequestDTO.PageRequestDTO dto, Long memberId) {
        Pageable pageable = dto.getPageable();

        Page<Member> memberPage = memberRepository.findAllByIdNot(memberId, pageable); // 본인 제외
        
        return memberPage.map(member -> new MemberResponseDTO.ReadAllResponseDTO(
            member,
            friendService.getFriendshipStatus(memberId, member.getId())
        ));
    }

    // 회원 조회
    public Member getMemberById(Long id) {
        Optional<Member> opMember = memberRepository.findById(id);

        if (opMember.isEmpty()) {
            throw MemberException.MEMBER_NOT_FOUND.getMemberTaskException();
        }

        return opMember.get();
    }

    // username으로 회원 검색
    public Page<MemberResponseDTO.ReadAllResponseDTO> searchByUsername(String username, MemberRequestDTO.PageRequestDTO dto, Long memberId) {
        // 검색어 유효성 검사
        if (username == null || username.trim().isEmpty()) {
            throw MemberException.SEARCH_KEYWORD_EMPTY.getMemberTaskException();
        }
        
        // 검색어 공백 제거 및 정리
        String trimmedUsername = username.trim();
        
        // 최소 검색어 길이 체크
        if (trimmedUsername.length() < 2) {
            throw MemberException.SEARCH_KEYWORD_TOO_SHORT.getMemberTaskException();
        }
        
        Pageable pageable = dto.getPageable();
        Page<Member> memberPage = memberRepository.findByUsernameContainingIgnoreCase(trimmedUsername, pageable);
        
        if (memberPage.isEmpty()) {
            log.info("검색 결과가 없습니다. 검색어: {}", trimmedUsername);
            throw MemberException.SEARCH_RESULT_NOT_FOUND.getMemberTaskException();
        }
        
        return memberPage.map(member -> new MemberResponseDTO.ReadAllResponseDTO(
            member,
            friendService.getFriendshipStatus(memberId, member.getId())
        ));
    }

    // 회원 탈퇴
    @Transactional
    public void delete(Long id, String pw) {
        Optional<Member> opMember = memberRepository.findById(id);
        if (opMember.isPresent()) {
            Member member = opMember.get();

            if (!passwordEncoder.matches(pw, member.getPw())) {
                throw new SecurityException("비밀번호가 일치하지 않습니다.");
            }

            memberRepository.delete(member);
        } else {
            throw MemberException.MEMBER_NOT_REMOVED.getMemberTaskException();
        }
    }

    // 회원 정보 수정
    @Transactional
    public MemberResponseDTO.UpdateMyInfoResponseDTO update(MemberRequestDTO.UpdateMyInfoRequestDTO dto) {
        Optional<Member> opMember = memberRepository.findById(dto.getId());

        if (opMember.isPresent()) {
            Member member = opMember.get();
            if (dto.getPw() != null) {
                member.changePw(passwordEncoder.encode(dto.getPw()));
            }

            if (dto.getUsername() != null) {
                member.changeUsername(dto.getUsername());
            }
            memberRepository.save(member);

            return new MemberResponseDTO.UpdateMyInfoResponseDTO("회원 정보 수정이 완료되었습니다.");
        } else {
            throw MemberException.MEMBER_NOT_MODIFIED.getMemberTaskException();
        }
    }

    // 아이디 찾기
    public String findLoginIdByEmail(String email, LocalDate birth) {
        Optional<Member> opMember = memberRepository.findLoginIdByEmailAndBirth(email, birth);
        if (opMember.isPresent()) {
            String loginId = opMember.get().getLoginId();
            return maskLoginId(loginId);
        } else {
            throw MemberException.MEMBER_NOT_FOUND.getMemberTaskException();
        }
    }

    // 아이디 마스킹 처리
    private String maskLoginId(String loginId) {

        int maskLength = loginId.length() - 4;
        StringBuilder masked = new StringBuilder();
        masked.append(loginId.substring(0, 2)); // 앞의 두 글자는 그대로 보여줌

        for (int i = 0; i < maskLength; i++) {
            masked.append("*");
        }

        masked.append(loginId.substring(loginId.length() - 2));
        return masked.toString();
    }

    // 비밀번호 재설정
    @Transactional
    public String resetPw(String loginId, String email) {
        Optional<Member> opMember = memberRepository.findByLoginIdAndEmail(loginId, email);
        if (opMember.isPresent()) {
            Member member = opMember.get();
            String templatePw = PasswordUtil.generateTempPassword();
            member.changePw(passwordEncoder.encode(templatePw));
            memberRepository.save(member);

            return templatePw;
        } else {
            throw MemberException.MEMBER_NOT_FOUND.getMemberTaskException();
        }
    }

    // 주식 게임 플레이 횟수 증가
    @Transactional
    public void increaseBeginStockPlayCount(Member member) {
        member.increaseBeginStockPlayCount();
        memberRepository.save(member);
    }

    // 회원 활동 상태 확인
    public boolean checkMemberIsActive(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> MemberException.MEMBER_NOT_FOUND.getMemberTaskException());
        return member.isActive();
    }

    // 회원 활동 상태 변경
    @Transactional
    public void updateMemberActiveStatus(Long id, boolean isActive) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> MemberException.MEMBER_NOT_FOUND.getMemberTaskException());
                
        // 이미 같은 상태인 경우 처리
        if (member.isActive() == isActive) {
            String status = isActive ? "이미 활성화" : "이미 비활성화";
            log.info("회원 ID: {}는 {} 상태입니다.", id, status);
            return;
        }
        
        member.updateActiveStatus(isActive);
        memberRepository.save(member);
        log.info("회원 ID: {}의 활성 상태가 {}로 변경되었습니다.", id, isActive);
    }

    // 로그아웃 처리 시 활성 상태 변경
    @Transactional
    public void logout(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> MemberException.MEMBER_NOT_FOUND.getMemberTaskException());
                
        if (!member.isActive()) {
            log.info("회원 ID: {}는 이미 로그아웃 상태입니다.", id);
            return;
        }
        
        // 로그아웃 시 비활성 상태로 변경
        member.updateActiveStatus(false);
        
        // refresh token 제거
        member.updateRefreshToken(null, null); // null로 변경
        memberRepository.save(member);
        log.info("회원 ID: {}가 로그아웃 되었습니다.", id);
    }

    // 동시 로그인 방지를 위한 메서드 추가
    public void validateActiveStatus(String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> MemberException.MEMBER_NOT_FOUND.getMemberTaskException());
                
        if (member.isActive()) {
            log.warn("회원 ID: {}는 이미 다른 곳에서 로그인 중입니다.", loginId);
            throw MemberException.MEMBER_ALREADY_LOGGED_IN.getMemberTaskException();
        }
    }
}
