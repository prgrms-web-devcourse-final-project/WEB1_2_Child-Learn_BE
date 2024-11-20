package com.prgrms.ijuju.point.dto.request;

import com.prgrms.ijuju.point.entity.PointType;
import lombok.Data;

@Data
public class PointRequestDTO {
    private Long memberId;
    private Long pointAmount;
    private PointType pointType;
    private Long gameId;
    private Long stockId;
}