package com.prgrms.ijuju.domain.member.entity;

import com.prgrms.ijuju.domain.ranking.entity.Ranking;
import com.prgrms.ijuju.domain.avatar.entity.Avatar;
import com.prgrms.ijuju.domain.avatar.entity.Item;
import com.prgrms.ijuju.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
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

    @Setter
    @Column(nullable = false)
    private Long points = 1000L; // 초기 포인트 설정

    @Setter
    @Column(nullable = false)
    private Long coins = 1000L; // 초기 코인 설정

    @Column
    private String profileImage; // 프로필 이미지

    @Column
    private boolean isActive = true; // 회원 활동 상태

    // pw 초기화 관련
    private String resetPwToken;
    private LocalDateTime resetPwTokenExpiryDate;

    @Column(columnDefinition = "TEXT")
    private String refreshToken;

    @Column(nullable = false)
    private int BeginStockPlayCount = 0;

    @OneToOne(mappedBy = "member", orphanRemoval = true)
    private Ranking ranking;

    @Builder
    public Member(Long id, String loginId, String pw, String username, String email, LocalDate birth, Long points, Long coins, String profileImage, boolean isActive){
        this.id = id;
        this.loginId=loginId;
        this.pw=pw;
        this.username=username;
        this.email=email;
        this.birth=birth;
        this.points=points != null ? points : 1000L;
        this.coins=coins != null ? coins : 1000L;
        this.profileImage=profileImage;
        this.isActive=isActive;
    }

    // 회원의 아바타(착용한 아이템들을 포함)
    @OneToOne(mappedBy = "member")
    private Avatar avatar;

    // 아이템을 여러 사용자가 소유
    @ManyToMany(mappedBy = "owners")
    private Set<Item> items = new HashSet<>();

    // 변경 가능한 회원 정보 : 별명(username), 비밀번호(pw)

    public void changeUsername(String username){
        this.username=username;
    }

    public void changePw(String pw){
        this.pw=pw;
    }

    public void updateRefreshToken(String refreshToken, LocalDateTime expiryDate){
        this.refreshToken=refreshToken;
        this.resetPwTokenExpiryDate=expiryDate;
    }

    public void increaseBeginStockPlayCount() {
        this.BeginStockPlayCount++;
    }

    public void changeRanking(Ranking ranking) {
        this.ranking = ranking;
    }

    public void getRemainingCoins(Long coins, Long price) {
        Long remainCoins = coins - price;
        this.coins = remainCoins;
    }

}
