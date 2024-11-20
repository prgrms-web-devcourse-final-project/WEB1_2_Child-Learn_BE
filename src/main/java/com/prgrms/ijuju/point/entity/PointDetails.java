package com.prgrms.ijuju.point.entity;

import com.prgrms.ijuju.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.prgrms.ijuju.minigame.entity.MiniGame;
import com.prgrms.ijuju.stock.entity.Stock;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class PointDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointDetailsId;

    @ManyToOne
    @JoinColumn(name = "point_id", nullable = false)
    private Point point;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    private PointType pointType;

    private Long pointAmount;

    @Enumerated(EnumType.STRING)
    private PointStatus pointStatus;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private MiniGame miniGame;

    @ManyToOne
    @JoinColumn(name = "adv_stock_id")
    private Stock stock;

    @ManyToOne
    @JoinColumn(name = "coin_details_id")
    private CoinDetails coinDetails;
}
