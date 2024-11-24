package com.prgrms.ijuju.domain.point.dto.request;

import com.prgrms.ijuju.domain.point.entity.PointStatus;
import com.prgrms.ijuju.domain.point.entity.PointType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PointRequestDTO { // 게임 및 투자 후 사용/획득한 포인트 등록 요청 DTO
    private Long memberId;
    private Long pointAmount;
    private PointType pointType;
    private PointStatus pointStatus;
}