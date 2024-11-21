package com.prgrms.ijuju.domain.member.dto.request;

import com.prgrms.ijuju.domain.member.entity.Member;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

public class MemberRequestDTO {

    @Getter
    @Builder
    @Data
    public static class CreateRequestDTO {

        @NotBlank(message = "로그인 ID는 반드시 입력해야 합니다.")
        @Size(min=5, max = 20)
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
        @Size(min=2, max = 30)
        private String username;

        @NotNull(message = "생년월일(YYYY-MM-DD) 8자리를 반드시 입력해야 합니다")
        private LocalDate birth;

        public Member toEntity(){
            return Member.builder()
                    .loginId(loginId)
                    .pw(pw)
                    .email(email)
                    .username(username)
                    .birth(birth)
                    .build();
        }


    }
}
