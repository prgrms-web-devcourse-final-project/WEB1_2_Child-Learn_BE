package com.prgrms.ijuju.domain.member.dto.response;

import com.prgrms.ijuju.domain.wallet.entity.Wallet;
import com.prgrms.ijuju.domain.friend.entity.FriendshipStatus;
import com.prgrms.ijuju.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;


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
        private LocalDateTime expiryAt;

        public RefreshAccessTokenResponseDTO(String accessToken, String message, LocalDateTime expiryAt) {
            this.accessToken = accessToken;
            this.message = message;
            this.expiryAt = expiryAt;
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
        private Long currentPoints;
        private Long currentCoins;
        private boolean isActive;

        public ReadMyInfoResponseDTO(Member member, Wallet wallet) {
            this.id = member.getId();
            this.loginId = member.getLoginId();
            this.email = member.getEmail();
            this.pw = member.getPw();
            this.username = member.getUsername();
            this.createdAt = member.getCreatedAt();
            this.updatedAt = member.getUpdatedAt();
            this.birth = member.getBirth();
            this.currentPoints = wallet.getCurrentPoints();
            this.currentCoins = wallet.getCurrentCoins();
            this.isActive = member.isActive();
        }
    }

    // 다른 회원의 정보 조회
    @Data
    public static class ReadOthersInfoResponseDTO {
        private long id;
        private String loginId;
        private String username;
        private LocalDateTime createdAt;
        private Long currentPoints;

        public ReadOthersInfoResponseDTO(Member member, Wallet wallet) {
            this.id = member.getId();
            this.loginId = member.getLoginId();
            this.username = member.getUsername();
            this.createdAt = member.getCreatedAt();
            this.currentPoints = wallet.getCurrentPoints();
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
        private FriendshipStatus friendshipStatus;

        public ReadAllResponseDTO(Member member, FriendshipStatus friendshipStatus) {
            this.id = member.getId();
            this.loginId = member.getLoginId();
            this.username = member.getUsername();
            this.friendshipStatus = friendshipStatus;
        }
    }

    // OAuth2 (수정중입니다)
    @Data
    public static class OAuth2ResponseDTO {
        private Map<String, Object> attribute;
        private String provider = "kakao";
        private String providerId;
        private String email;
        private String name;
        private String profileImage;

        public OAuth2ResponseDTO(Map<String, Object> attribute) {
            this.attribute = attribute;
            this.providerId = (String) attribute.get("id");
            this.email = (String) attribute.get("email");
            this.name = (String) attribute.get("name");
            this.profileImage = (String) attribute.get("profile_image");
        }
    }

    // ProfileImage
    @Data
    @AllArgsConstructor
    public static class updateProfileImageDTO {
        private String message;
    }
}
