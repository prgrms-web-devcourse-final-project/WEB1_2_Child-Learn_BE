package com.prgrms.ijuju.domain.article.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
/**
 * AdvTrendAnalyzer 에서 사용되는, 중간다리 역할의 데이터입니다.
 * Adv 같은 경우는 날짜를 timestamp 로 받기 때문에 하루단위로 묶어야 합니다
 * MidStock 은 쓰지 않습니다. MidStock 은 처음부터 LocalData 로 이루어져있기 때문입니다
 */
@Data
@AllArgsConstructor
public class DailyTrend {
    private LocalDate date;
    private String trendType; // "UP", "DOWN", "STABLE"
    private double percentageChange;
}