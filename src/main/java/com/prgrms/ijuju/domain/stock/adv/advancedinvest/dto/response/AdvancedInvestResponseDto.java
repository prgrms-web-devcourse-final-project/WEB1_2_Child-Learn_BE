package com.prgrms.ijuju.domain.stock.adv.advancedinvest.dto.response;

import com.prgrms.ijuju.domain.stock.adv.advancedinvest.entity.AdvancedInvest;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvancedInvestResponseDto {
    private Long advId;       // AdvancedInvest 게임 ID
    private Long memberId;    // 유저 ID
    private boolean paused;   // 일시정지 여부
    private boolean playedToday; // 오늘 게임 실행 여부
    private LocalDateTime startTime; // 게임 시작 시간
    private String remainingTime;  // 남은 시간 (ISO 8601 포맷)

    public static AdvancedInvestResponseDto from(AdvancedInvest advancedInvest) {
        return AdvancedInvestResponseDto.builder()
                .advId(advancedInvest.getId())
                .memberId(advancedInvest.getMemberId())
                .paused(advancedInvest.isPaused())
                .playedToday(advancedInvest.isPlayedToday())
                .startTime(advancedInvest.getStartTime())
                .remainingTime(advancedInvest.getRemainingTime() != null
                        ? advancedInvest.getRemainingTime().toString() : null)
                .build();
    }
}