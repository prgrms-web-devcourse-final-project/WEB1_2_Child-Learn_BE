package com.prgrms.ijuju.domain.point.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

import com.prgrms.ijuju.domain.point.entity.PointStatus;
import com.prgrms.ijuju.domain.point.entity.PointType;
import lombok.NoArgsConstructor;
import com.prgrms.ijuju.domain.point.entity.GameType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointDetailsResponseDTO { // 포인트 기록 응답 DTO
    private Long id;
    private PointType pointType;
    private Long pointAmount;
    private PointStatus pointStatus;
    private LocalDateTime createdAt;
    private GameType gameType;
}