package com.prgrms.ijuju.domain.member.entity;

import com.prgrms.ijuju.domain.avatar.entity.Avatar;
import com.prgrms.ijuju.domain.avatar.entity.Purchase;
import com.prgrms.ijuju.domain.wallet.entity.Wallet;
import com.prgrms.ijuju.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member extends BaseTimeEntity {

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

    @Column(nullable = false)
    private LocalDate birth;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @Column
    private String profileImage; // 프로필 이미지

    @Column(nullable = false)
    private boolean isActive = false;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    private LocalDateTime resetPwTokenExpiryAt;

    @Column(columnDefinition = "TEXT")
    private String refreshToken;

    @Column(nullable = false)
    private int BeginStockPlayCount = 0;

    @Builder
    public Member(Long id, String loginId, String pw, String username, String email, LocalDate birth, String profileImage, Avatar avatar, Role role){
        this.id = id;
        this.loginId=loginId;
        this.pw=pw;
        this.username=username;
        this.email=email;
        this.birth=birth;
        this.profileImage=profileImage;
        this.avatar=avatar;
        this.role=role;
    }

    // 회원의 아바타(착용한 아이템들을 포함)
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    @JoinColumn(name = "avatar_id")
    private Avatar avatar;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Purchase> purchases = new ArrayList<>();

    // 변경 가능한 회원 정보 : 별명(username), 비밀번호(pw)

    public void changeUsername(String username){
        this.username=username;
    }

    public void changePw(String pw){
        this.pw=pw;
    }

    public void updateRefreshToken(String refreshToken, LocalDateTime expiryAt){
        this.refreshToken=refreshToken;
        this.resetPwTokenExpiryAt=expiryAt;
    }

    public void increaseBeginStockPlayCount() {
        this.BeginStockPlayCount++;
    }

    public void changeAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public void getRemainingCoins(Long coins, Long price) {
        Long remainCoins = coins - price;
        this.wallet.subtractCoins(remainCoins);
    }

    public void updateActiveStatus(boolean isActive) {
        this.isActive = isActive;
    }

    public void changeProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    ///**** OAuth2 로그인에서만 사용 *****///
    public void changeEmail(String email) {
        this.email=email;
    }

}
