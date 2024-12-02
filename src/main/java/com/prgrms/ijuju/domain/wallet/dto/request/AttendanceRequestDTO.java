package com.prgrms.ijuju.domain.wallet.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRequestDTO {
    
    private Long memberId;
    private Long points;
    private boolean isCheckIn;
    private LocalDateTime startOfDay;
    private LocalDateTime endOfDay;
}
