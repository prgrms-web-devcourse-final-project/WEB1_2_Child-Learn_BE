package com.prgrms.ijuju.domain.stock.begin.entity;

import com.prgrms.ijuju.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "limit_begin_stock")
@Entity
public class LimitBeginStock {
    @Id
    private Long memberId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Member player;

    @Column(nullable = false)
    private LocalDate lastPlayedDate = LocalDate.parse("2024-11-11");

    public LimitBeginStock(Member player) {
        this.player = player;
    }

    public void updateLastPlayedDate() {
        this.lastPlayedDate = LocalDate.now();
    }
}
