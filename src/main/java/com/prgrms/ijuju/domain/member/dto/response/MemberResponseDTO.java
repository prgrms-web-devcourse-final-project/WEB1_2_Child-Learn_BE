package com.prgrms.ijuju.domain.member.dto.response;

import com.prgrms.ijuju.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class MemberResponseDTO {

    // 회원가입
    @Data
    public static class CreateResponseDTO {
        private String message;

        public CreateResponseDTO(String message) {
            this.message = message;
        }
    }

    // 로그인
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginResponseDTO {
        private long id;
        private String loginId;
        private String username;
        private LocalDate birth;
        private String accessToken;
        //private String refreshToken;

        public LoginResponseDTO(Member member) {
            this.id= member.getId();
            this.loginId= member.getLoginId();
            this.username= member.getUsername();
            this.birth=member.getBirth();
        }
    }

    // refresh AccessToken
    @Data
    public static class RefreshAccessTokenResponseDTO {
        private String accessToken;
        private String message;

        public RefreshAccessTokenResponseDTO(String accessToken, String message) {
            this.accessToken = accessToken;
            this.message = message;
        }
    }

    // 로그아웃
    @Data
    public static class LogoutResponseDTO {
        private String message;

        public LogoutResponseDTO(String message) {
            this.message = message;
        }
    }

    // 나의 회원 정보 조회
    @Data
    @AllArgsConstructor
    public static class ReadMyInfoResponseDTO {
        private long id;
        private String loginId;
        private String email;
        private String pw;
        private String username;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDate birth;
        private long points;

        public ReadMyInfoResponseDTO(Member member) {
            this.id = member.getId();
            this.loginId = member.getLoginId();
            this.email = member.getEmail();
            this.pw = member.getPw();
            this.username = member.getUsername();
//            this.createdAt = member.getCreatedAt();
//            this.updatedAt = member.getUpdatedAt();
            this.birth = member.getBirth();
            this.points = member.getPoints();
        }
    }

    // 다른 회원의 정보 조회
    @Data
    public static class ReadOthersInfoResponseDTO {
        private long id;
        private String loginId;
        private String username;
        private LocalDateTime createdAt;
        private long points;

        public ReadOthersInfoResponseDTO(Member member) {
            this.id = member.getId();
            this.loginId = member.getLoginId();
            this.username = member.getUsername();
//            this.createdAt = member.getCreatedAt();
            this.points = member.getPoints();
        }
    }

    // 회원 정보 수정
    @Data
    @AllArgsConstructor
    public static class UpdateMyInfoResponseDTO {

        private String message;
    }

    // 모든 회원 목록 조회
    @Data
    public static class ReadAllResponseDTO {
        private Long id;
        private String loginId;
        private String username;

        public ReadAllResponseDTO(Member member) {
            this.id = member.getId();
            this.loginId = member.getLoginId();
            this.username = member.getUsername();
        }
    }
}
