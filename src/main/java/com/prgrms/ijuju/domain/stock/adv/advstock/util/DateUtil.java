package com.prgrms.ijuju.domain.stock.adv.advstock.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Reference 데이터의 시작일 (4주 전 수요일)
    public static String getReferenceStartDate() {
        return LocalDate.now().minusWeeks(4).format(FORMATTER); // 4주 전 수요일
    }

    // Reference 데이터의 종료일 (3주 전 화요일)
    public static String getReferenceEndDate() {
        return LocalDate.now().minusWeeks(3).minusDays(1).format(FORMATTER); // 3주 전 화요일
    }

    // Live 데이터의 하루 (3주 전 수요일)
    public static String getLiveDate() {
        return LocalDate.now().minusWeeks(3).format(FORMATTER); // 3주 전 수요일
    }

    // Forecast 데이터 시작일 (3주 전 목요일)
    public static String getForecastStartDate() {
        return LocalDate.now().minusWeeks(3).plusDays(1).format(FORMATTER); // 3주 전 목요일
    }

    // Forecast 데이터 종료일 (하루 전 화요일)
    public static String getForecastEndDate() {
        return LocalDate.now().minusDays(1).format(FORMATTER); // 하루 전 화요일
    }
}