package com.prgrms.ijuju.domain.stock.adv.advancedinvest.dto.response;

import com.prgrms.ijuju.domain.stock.adv.advancedinvest.entity.AdvancedInvest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/* Http Request 용 dto 로 더이상 사용하지 않습니다

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdvancedInvestResponseDto {
    private Long advId;
    private Long memberId;
    private boolean paused;
    private boolean playedToday;

    public static AdvancedInvestResponseDto from(AdvancedInvest advancedInvest) {
        return AdvancedInvestResponseDto.builder()
                .advId(advancedInvest.getId())
                .memberId(advancedInvest.getMember().getId())
                .paused(advancedInvest.isPaused())
                .playedToday(advancedInvest.isPlayedToday())
                .build();
    }
}

 */