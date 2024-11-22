package com.prgrms.ijuju.domain.point.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

import com.prgrms.ijuju.domain.point.entity.PointStatus;
import com.prgrms.ijuju.domain.point.entity.PointType;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointDetailsDTO { // 포인트 상세 응답 DTO
    @NotNull
    private Long pointDetailsId;
    private PointType pointType;
    private Long pointAmount;
    private PointStatus pointStatus;
    private LocalDateTime createdAt;
}
