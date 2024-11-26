package com.prgrms.ijuju.domain.minigame.flipcard.entity;

import com.prgrms.ijuju.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LimitCardGame {

    @Id
    @Column(name = "member_id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // Member의 ID를 PK이자 FK로 사용
    @JoinColumn(name = "member_id")
    private Member member;

    // 마지막 플레이 시간, 초기값은 현재시간 1달 전으로 설정
    @Column(nullable = false)
    private LocalDateTime beginLastPlayed = LocalDateTime.now().minusMonths(1);;

    @Column(nullable = false)
    private LocalDateTime midLastPlayed = LocalDateTime.now().minusMonths(1);;

    @Column(nullable = false)
    private LocalDateTime advLastPlayed = LocalDateTime.now().minusMonths(1);;

    public LimitCardGame(Member member) {
        this.member = member;
    }

    public void updateBeginLastPlayed() {
        this.beginLastPlayed = LocalDateTime.now();
    }

    public void updateMidLastPlayed() {
        this.midLastPlayed = LocalDateTime.now();
    }

    public void updateAdvLastPlayed() {
        this.advLastPlayed = LocalDateTime.now();
    }
}
