package com.prgrms.ijuju.domain.member.dto.request;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.entity.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

public class MemberRequestDTO {


    // 회원가입
    @Builder
    @Data
    public static class CreateRequestDTO {

        @NotBlank(message = "로그인 ID는 반드시 입력해야 합니다.")
        @Size(min = 5, max = 20)
        private String loginId;

        @NotBlank(message = "비밀번호는 반드시 입력해야 합니다.")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,30}$",
                message = "비밀번호는 최소 8자 이상 30자 이하여야 하고, 적어도 하나의 영문자, 숫자, 특수문자를 포함해야 합니다."
        )
        private String pw;

        @NotBlank(message = "이메일은 반드시 입력해야 합니다.")
        @Email
        @Size(max = 50)
        private String email;

        @NotBlank(message = "닉네임은 반드시 입력해야 합니다")
        @Size(min = 2, max = 8)
        private String username;

        @NotNull(message = "생년월일(YYYY-MM-DD) 8자리를 반드시 입력해야 합니다")
        private LocalDate birth;

        public Member toEntity() {
            return Member.builder()
                    .loginId(loginId)
                    .pw(pw)
                    .email(email)
                    .username(username)
                    .birth(birth)
                    .role(Role.USER)
                    .build();
        }
    }

    // 회원가입 시 아이디 중복체크
    @Data
    public static class CheckLoginIdRequestDTO {

        @Size(min = 5, max = 20)
        private String loginId;
    }

    // 로그인
    @Data
    public static class LoginRequestDTO {

        private String loginId;

        private String pw;
    }

    // refreshToken 요청
    @Data
    public static class RefreshAccessTokenRequestDTO {
        private String refreshToken;
    }

    // 유저네임 업데이트
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateUsernameRequestDTO {
        private String username;
    }

    // 비밀번호 업데이트
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdatePwRequestDTO {
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,30}$",
                message = "비밀번호는 최소 8자 이상 30자 이하여야 하고, 적어도 하나의 영문자, 숫자, 특수문자를 포함해야 합니다."
        )
        private String pw;
    }

    // 아이디 찾기
    @Data
    public static class FindLoginIdRequestDTO {

        private String email;
        private LocalDate birth;
    }

    // 비밀번호 재설정
    @Data
    public static class ResetPwRequestDTO {

        @NotBlank
        private String loginId;

        @NotBlank
        @Email
        private String email;
    }

    // 회원 탈퇴
    @Data
    public static class DeleteRequestDTO {
        private String email;
        private String pw;
    }

    // 회원 목록 페이징처리
    @Data
    public static class PageRequestDTO {
        private int page;
        private int size;
        private String sortField;
        private String sortDirection;

        public PageRequestDTO() {
            this.page = 0;
            this.size = 10;
            this.sortField = "id";
            this.sortDirection = "ASC";
        }

        public Pageable getPageable() {
            Sort sort = Sort.by(Sort.Direction.fromString(this.sortDirection), this.sortField);
            return PageRequest.of(this.page, this.size, sort);
        }
    }

}
