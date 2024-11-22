package com.prgrms.ijuju.domain.point.dto.request;

import com.prgrms.ijuju.domain.point.entity.PointType;

import lombok.Data;

@Data
public class PointRequestDTO { // 포인트 요청 DTO
    private Long memberId;
    private Long pointAmount;
    private PointType pointType;
    private Long gameId;
    private Long stockId;
}