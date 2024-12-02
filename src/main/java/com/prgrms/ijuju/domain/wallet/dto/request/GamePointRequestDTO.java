package com.prgrms.ijuju.domain.wallet.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import com.prgrms.ijuju.domain.wallet.entity.GameType;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GamePointRequestDTO {
    
    private Long memberId;
    private Long points;
    private boolean isWin;
    private GameType gameType;
}
