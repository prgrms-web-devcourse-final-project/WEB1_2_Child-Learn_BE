package com.prgrms.ijuju.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String loginId;

    private String pw;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false)
    private Long points = 1000L; // 초기 금액 설정

    // pw 초기화 관련
    private String resetPwToken;
    private LocalDateTime resetPwTokenExpiryDate;

    @Column(columnDefinition = "TEXT")
    private String refreshToken;

    @Builder
    public Member(String loginId, String pw, String username, String email, LocalDate birth, Long points){
        this.loginId=loginId;
        this.pw=pw;
        this.username=username;
        this.email=email;
        this.birth=birth;
        this.points=points != null ? points : 1000L;
    }

    // 변경 가능한 회원 정보 : 별명(username), 비밀번호(pw)

    public void changeUsername(String username){
        this.username=username;
    }

    public void changePw(String pw){
        this.pw=pw;
        //, PasswordEncoder passwordEncoder :
        //this.pw=passwordEncoder.encode(pw);
    }

    public void updateRefreshToken(String refreshToken, LocalDateTime expiryDate){
        this.refreshToken=refreshToken;
        this.resetPwTokenExpiryDate=expiryDate;
    }

}
