package com.prgrms.ijuju.domain.article.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DailyTrend {
    private LocalDate date;
    private String trendType; // "UP", "DOWN", "STABLE"
    private double percentageChange;
}