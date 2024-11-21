package com.prgrms.ijuju.domain.member.controller;

import com.prgrms.ijuju.domain.member.dto.request.MemberRequestDTO;
import com.prgrms.ijuju.domain.member.dto.response.MemberResponseDTO;
import com.prgrms.ijuju.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
