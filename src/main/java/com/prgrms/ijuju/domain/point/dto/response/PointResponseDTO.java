package com.prgrms.ijuju.domain.point.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointResponseDTO { // 현재 포인트와 코인 응답 DTO
    private Long currentPoints;
    private Long currentCoins;
}

