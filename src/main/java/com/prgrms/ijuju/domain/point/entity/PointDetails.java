package com.prgrms.ijuju.domain.point.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.validation.constraints.NotNull;

import com.prgrms.ijuju.domain.member.entity.Member;

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
    private Long id;

    @ManyToOne
    @JoinColumn(name = "point_id", nullable = false)
    @NotNull
    private Point point;


    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    @NotNull
    private Member member;


    @Enumerated(EnumType.STRING)
    private PointType pointType;

    private Long pointAmount;

    @Enumerated(EnumType.STRING)
    private PointStatus pointStatus;

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private GamePoint gamePoint;

    @ManyToOne
    @JoinColumn(name = "adv_stock_id")
    private StockPoint stockPoint;

    @ManyToOne
    @JoinColumn(name = "coin_details_id")
    private CoinDetails coinDetails;

    private LocalDateTime eventTime;
}
