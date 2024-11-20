package com.prgrms.ijuju.point.dto.response;

import com.prgrms.ijuju.point.entity.PointStatus;
import com.prgrms.ijuju.point.entity.PointType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PointDetailsDTO {
    private Long pointDetailsId;
    private PointType pointType;
    private Long pointAmount;
    private PointStatus pointStatus;
    private LocalDateTime createdAt;
}
