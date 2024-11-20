package com.prgrms.ijuju.domain.member.service;

import com.prgrms.ijuju.domain.member.dto.request.MemberRequestDTO;
import com.prgrms.ijuju.domain.member.dto.response.MemberResponseDTO;
import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.exception.MemberException;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberResponseDTO.CreateResponseDTO create(MemberRequestDTO.CreateRequestDTO dto) {
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
}
