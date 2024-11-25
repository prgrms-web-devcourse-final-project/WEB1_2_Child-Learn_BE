package com.prgrms.ijuju.domain.stock.adv.advancedinvest.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvancedInvestRequestDto {
    @NotNull(message = "Member ID는 필수입니다.")
    private Long memberId; // 게임을 시작하는 유저의 ID
}