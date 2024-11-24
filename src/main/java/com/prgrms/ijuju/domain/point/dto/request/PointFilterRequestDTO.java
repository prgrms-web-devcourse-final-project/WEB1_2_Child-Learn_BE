package com.prgrms.ijuju.domain.point.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PointFilterRequestDTO { // 포인트 기록 필터 요청 DTO
    private Long memberId;
    private String pointType;
    private String pointStatus;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
} 