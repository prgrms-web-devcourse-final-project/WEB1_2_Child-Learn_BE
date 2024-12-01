package com.prgrms.ijuju.domain.minigame.flipcard.entity;

import com.prgrms.ijuju.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    private LocalDate beginLastPlayed = LocalDate.now().minusMonths(1);;

    @Column(nullable = false)
    private LocalDate midLastPlayed = LocalDate.now().minusMonths(1);;

    @Column(nullable = false)
    private LocalDate advLastPlayed = LocalDate.now().minusMonths(1);;

    public void updateBeginLastPlayed() {

        this.beginLastPlayed = LocalDate.now();
    }

    public void updateMidLastPlayed() {

        this.midLastPlayed = LocalDate.now();
    }

    public void updateAdvLastPlayed() {
        this.advLastPlayed = LocalDate.now();
    }

    @Builder
    public LimitCardGame(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null");
        }
        this.member = member;
//        this.id = member.getId(); // FK와 PK를 동일하게 설정하면 null identifier오류 발생
    }
}
