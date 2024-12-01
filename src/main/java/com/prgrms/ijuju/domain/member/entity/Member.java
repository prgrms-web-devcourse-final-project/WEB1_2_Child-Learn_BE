package com.prgrms.ijuju.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.prgrms.ijuju.domain.wallet.entity.Wallet;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "member")
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

    @Column
    private String profileImage; // 프로필 이미지

    @Column
    private boolean isActive = true; // 회원 활동 상태

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    // pw 초기화 관련
    private String resetPwToken;
    private LocalDateTime resetPwTokenExpiryDate;

    @Column(columnDefinition = "TEXT")
    private String refreshToken;

    @Column(nullable = false)
    private int BeginStockPlayCount = 0;

    @Builder
    public Member(Long id, String loginId, String pw, String username, String email, LocalDate birth, String profileImage, boolean isActive){
        this.id = id;
        this.loginId=loginId;
        this.pw=pw;
        this.username=username;
        this.email=email;
        this.birth=birth;
        this.profileImage="img.png";
        this.isActive=isActive;
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

    public void increaseBeginStockPlayCount() {
        this.BeginStockPlayCount++;
    }
}
