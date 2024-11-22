package com.prgrms.ijuju.domain.point.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PointFilterRequestDTO { // 포인트 필터 요청 DTO
    private Long memberId; // 회원 ID
    private String pointType; // 포인트 유형
    private String pointStatus; // 포인트 상태
    private LocalDateTime startDate; // 시작 날짜
    private LocalDateTime endDate; // 종료 날짜
} 