package com.prgrms.ijuju.domain.stock.adv.advancedinvest.entity;

import com.prgrms.ijuju.domain.stock.adv.advancedinvest.constant.GameStage;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AdvancedInvest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId; // 유저 ID

    @Enumerated(EnumType.STRING)
    private GameStage currentStage; // 현재 게임 단계 (PRE_MARKET, MARKET, POST_MARKET)

    private boolean paused; // 일시정지 상태

    private boolean playedToday; // 오늘 게임 진행 여부

    private LocalDateTime startTime; // 게임 시작 시간

    private Duration remainingTime; // 남은 시간
}
