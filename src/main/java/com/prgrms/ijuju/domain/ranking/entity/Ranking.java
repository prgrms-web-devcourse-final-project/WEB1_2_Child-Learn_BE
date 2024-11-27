package com.prgrms.ijuju.domain.ranking.entity;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ranking extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rank_id")
    Long id;

    @NotNull
    LocalDateTime weekStart;

    @NotNull
    LocalDateTime weekEnd;

    long weeklyPoints;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    Member member;

    @Builder
    public Ranking(LocalDateTime weekStart, LocalDateTime weekEnd, long weeklyPoints, Member member) {
        this.weekStart = weekStart;
        this.weekEnd = weekEnd;
        this.weeklyPoints = weeklyPoints;
        changeMember(member);
    }

    public void changeWeeklyPoints(long weeklyPoints) {
        this.weeklyPoints = weeklyPoints;
    }

    public void changeMember(Member member) {
        this.member = member;
        if (member != null && member.getRanking() != this) {
            member.changeRanking(this);
        }
    }

}
