package com.prgrms.ijuju.domain.ranking.entity;

import com.prgrms.ijuju.domain.member.entity.Member;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Ranking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rank_id")
    Long id;

    LocalDate weekStart;

    LocalDate weekEnd;

    long weeklyPoints;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    Member member;

}
