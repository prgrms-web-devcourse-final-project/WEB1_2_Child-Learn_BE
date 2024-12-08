package com.prgrms.ijuju.domain.ranking.entity;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.global.common.entity.BaseTimeEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ranking extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rank_id")
    private Long id;

    @NotNull
    private LocalDateTime weekStart;

    @NotNull
    private LocalDateTime weekEnd;

    private Long weeklyPoints;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE) // 추가: Member 삭제시 같이 삭제되도록 설정
    private Member member;

    @Builder
    public Ranking(LocalDateTime weekStart, LocalDateTime weekEnd, Long weeklyPoints, Member member) {
        this.weekStart = weekStart;
        this.weekEnd = weekEnd;
        this.weeklyPoints = weeklyPoints;
        this.member = member;
    }

    public void changeWeeklyPoints(long weeklyPoints) {
        this.weeklyPoints = weeklyPoints;
    }
}
