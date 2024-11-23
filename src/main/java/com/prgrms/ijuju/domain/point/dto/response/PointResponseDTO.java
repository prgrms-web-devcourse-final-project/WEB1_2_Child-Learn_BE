package com.prgrms.ijuju.domain.point.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointResponseDTO { // 포인트 응답 DTO
    private Long currentPoints;
    private Long currentCoins;
    private List<PointDetailsResponseDTO> pointHistory;
}

