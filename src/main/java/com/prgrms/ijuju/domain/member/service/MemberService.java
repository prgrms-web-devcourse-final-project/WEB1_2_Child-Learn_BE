package com.prgrms.ijuju.domain.member.service;

import com.prgrms.ijuju.domain.avatar.entity.Avatar;
import com.prgrms.ijuju.domain.avatar.repository.AvatarRepository;
import com.prgrms.ijuju.domain.avatar.service.FileStorageService;
import com.prgrms.ijuju.domain.friend.service.FriendService;
import com.prgrms.ijuju.domain.member.dto.request.MemberRequestDTO;
import com.prgrms.ijuju.domain.member.dto.response.MemberResponseDTO;
import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.exception.MemberErrorCode;
import com.prgrms.ijuju.domain.member.exception.MemberException;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.wallet.entity.Wallet;
import com.prgrms.ijuju.domain.wallet.exception.WalletException;
import com.prgrms.ijuju.domain.wallet.repository.WalletRepository;
import com.prgrms.ijuju.global.exception.CustomException;
import com.prgrms.ijuju.global.util.JwtUtil;
import com.prgrms.ijuju.global.util.PasswordUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private final FileStorageService fileStorageService;

    // 회원가입
    @Transactional
    public MemberResponseDTO.CreateResponseDTO create(MemberRequestDTO.CreateRequestDTO dto) {
        log.info("회원가입 요청 시작 : {} ", dto.getLoginId());

        // 입력한 loginId로 가입한 회원이 있는지 확인
        log.info("아이디 중복 확인 : {}", dto.getLoginId());
        if (checkLoginId(dto.getLoginId())) {
            log.error("이미 존재하는 아이디 : {}", dto.getLoginId());
            throw new MemberException(MemberErrorCode.LOGINID_IS_DUPLICATED);
        }

        // 입력한 이메일로 가입한 회원이 있는지 확인
        if (checkEmail(dto.getEmail())) {
            log.error("해당 이메일로 가입한 회원이 존재합니다 : {}", dto.getEmail());
            throw new MemberException(MemberErrorCode.EMAIL_IS_DUPLICATED);
        }

        // 별명 중복 확인 로직
        if (checkUsername(dto.getUsername())) {
            log.error("이미 존재하는 닉네임 : {}", dto.getUsername());
            throw new MemberException(MemberErrorCode.USERNAME_IS_DUPLICATED);
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

        savedMember.changeAvatar(newAvatar);

        avatarRepository.save(newAvatar);
        memberRepository.save(savedMember);

        return new MemberResponseDTO.CreateResponseDTO("회원가입이 완료되었습니다.");
    }

    // 회원가입 시 같은 아이디 검증 메서드
    public boolean checkLoginId(String loginId) {
        log.info("아이디 중복 체크 : {}", loginId);
        return memberRepository.findByLoginId(loginId).isPresent();
    }

    // 이메일 중복 검증 메서드
    public boolean checkEmail(String email) {
        log.info("이메일 중복 체크 : {}", email);
        return memberRepository.findByEmail(email).isPresent();
    }

    // 별명 중복 검증 메서드
    public boolean checkUsername(String username) {
        log.info("별명 중복 체크 : {}", username);
        return memberRepository.findByUsername(username).isPresent();
    }

    // 로그인
    public MemberResponseDTO.LoginResponseDTO loginIdAndPw(String loginId, String pw, HttpServletResponse response) {
        // 아이디 입력 검증
        if (loginId == null || loginId.trim().isEmpty()) {
            throw new MemberException(MemberErrorCode.LOGIN_ID_REQUIRED);
        }

        // 비밀번호 입력 검증
        if (pw == null || pw.trim().isEmpty()) {
            throw new MemberException(MemberErrorCode.PASSWORD_REQUIRED);
        }

        // 동시 로그인 검증
        validateActiveStatus(loginId);

        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(pw, member.getPw())) {
            throw new MemberException(MemberErrorCode.MEMBER_LOGIN_DENIED);
        }

        // 로그인 시 활성 상태로 변경
        member.updateActiveStatus(true);
        memberRepository.save(member);

        String accessToken = generateAccessToken(member.getId(), loginId);
        String refreshToken = generateRefreshToken(member.getId(), loginId);

        addRefreshTokenToCookie(refreshToken, response);

        LocalDateTime expiryAt = LocalDateTime.now().plusDays(3);
        setRefreshToken(member.getId(), refreshToken, expiryAt);

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
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_LOGIN_DENIED));

        // 리프레시 토큰이 만료되었다면 로그아웃
//        try {
//            Claims claims = JwtUtil.decode(refreshToken);
//        } catch (ExpiredJwtException e) {
//            throw new MemberException(MemberErrorCode.MEMBER_REFRESHTOKEN_EXPIRED);
//        }
        Claims claims = JwtUtil.decode(refreshToken); // decode가 만약 만료된 토큰이라면 null을 반환한다고 가정
        if (claims == null || claims.getExpiration().before(new Date())) {
            // 리프레시 토큰이 만료된 경우
            throw new MemberException(MemberErrorCode.MEMBER_REFRESHTOKEN_EXPIRED);
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
    public void setRefreshToken(Long id, String refreshToken, LocalDateTime expiryAt) {
        Member member = memberRepository.findById(id).get();
        member.updateRefreshToken(refreshToken, expiryAt);
    }

    // 나의 회원 정보 조회
    public MemberResponseDTO.ReadMyInfoResponseDTO readMyInfo(Long id) {
        Optional<Member> opMember = memberRepository.findById(id);
        if (opMember.isPresent()) {
            Member member = opMember.get();
            Wallet wallet = walletRepository.findByMemberId(member.getId())
                    .orElseThrow(() -> new CustomException(WalletException.WALLET_NOT_FOUND));
            return new MemberResponseDTO.ReadMyInfoResponseDTO(member, wallet);
        } else {
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
        }
    }

    // 다른 회원 정보 조회
    public MemberResponseDTO.ReadOthersInfoResponseDTO readOthersInfo(Long id) {
        Optional<Member> opMember = memberRepository.findById(id);
        if (opMember.isPresent()) {
            Member member = opMember.get();
            Wallet wallet = walletRepository.findByMemberId(member.getId())
                    .orElseThrow(() -> new CustomException(WalletException.WALLET_NOT_FOUND));
            return new MemberResponseDTO.ReadOthersInfoResponseDTO(member, wallet);
        } else {
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
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
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        return opMember.get();
    }

    // username으로 회원 검색
    public Page<MemberResponseDTO.ReadAllResponseDTO> searchByUsername(String username, MemberRequestDTO.PageRequestDTO dto, Long memberId) {
        // 검색어 유효성 검사
        if (username == null || username.trim().isEmpty()) {
            throw new MemberException(MemberErrorCode.SEARCH_KEYWORD_EMPTY);
        }

        // 검색어 공백 제거 및 정리
        String trimmedUsername = username.trim();

        // 최소 검색어 길이 체크
        if (trimmedUsername.length() < 2) {
            throw new MemberException(MemberErrorCode.SEARCH_KEYWORD_TOO_SHORT);
        }

        Pageable pageable = dto.getPageable();
        Page<Member> memberPage = memberRepository.findByUsernameContainingIgnoreCase(trimmedUsername, pageable);

        if (memberPage.isEmpty()) {
            log.info("검색 결과가 없습니다. 검색어: {}", trimmedUsername);
            throw new MemberException(MemberErrorCode.SEARCH_RESULT_NOT_FOUND);
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
            throw new MemberException(MemberErrorCode.MEMBER_NOT_REMOVED);
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
            throw new MemberException(MemberErrorCode.MEMBER_NOT_MODIFIED);
        }
    }

    // 아이디 찾기
    public String findLoginIdByEmail(String email, LocalDate birth) {
        Optional<Member> opMember = memberRepository.findLoginIdByEmailAndBirth(email, birth);
        if (opMember.isPresent()) {
            String loginId = opMember.get().getLoginId();
            return maskLoginId(loginId);
        } else {
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
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
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
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
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        return member.isActive();
    }

    // 회원 활동 상태 변경
    @Transactional
    public void updateMemberActiveStatus(Long id, boolean isActive) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

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

    // 로그아웃
    @Transactional
    public void logout(Long id, HttpServletResponse response) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        if (!member.isActive()) {
            log.info("회원 ID: {}는 이미 로그아웃 상태입니다.", id);
            return;
        }

        // 쿠키에 있는 refreshToken 제거
        removeRefreshTokenToCookie(member.getRefreshToken(), response);

        // 로그아웃 시 비활성 상태로 변경
        member.updateActiveStatus(false);

        // refresh token 제거
        member.updateRefreshToken(null, null); // null로 변경
        memberRepository.save(member);
        log.info("회원 ID: {}가 로그아웃 되었습니다.", id);
    }

    public void removeRefreshTokenToCookie(String refreshToken, HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    // 동시 로그인 방지를 위한 메서드 추가
    public void validateActiveStatus(String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        if (member.isActive()) {
            log.warn("회원 ID: {}는 이미 다른 곳에서 로그인 중입니다.", loginId);
            throw new MemberException(MemberErrorCode.MEMBER_ALREADY_LOGGED_IN);
        }
    }

    // profileImage 저장
    @Transactional
    public String updateProfileImage(Long id, MultipartFile file) throws IOException {
        // 이미지 파일을 로컬에 저장하고 URl 경로로 반환받음
        String profileImageUrl = fileStorageService.storeFile(file);

        // 멤버 프로필 이미지 업데이트
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        member.changeProfileImage(profileImageUrl);

        // member 저장
        memberRepository.save(member);

        return profileImageUrl;
    }


}
