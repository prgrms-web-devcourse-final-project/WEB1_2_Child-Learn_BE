package com.prgrms.ijuju.domain.stock.adv.advancedinvest.entity;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.stock.adv.stockrecord.entity.StockRecord;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdvancedInvest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private long startTime;

    private boolean paused;
    private boolean playedToday;

    @OneToMany(mappedBy = "advancedInvest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockRecord> stockRecords;
}