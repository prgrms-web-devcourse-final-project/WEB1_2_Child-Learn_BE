package com.prgrms.ijuju.stock.adv.stock.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.Clock;

public class DateUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Reference 데이터의 시작일 (2주 전)
    public static String getReferenceStartDate() {
        return LocalDate.now().minusDays(14).format(FORMATTER); // 14일 전
    }

    // Reference 데이터의 종료일 (1주 전)
    public static String getReferenceEndDate() {
        return LocalDate.now().minusDays(7).format(FORMATTER); // 7일 전
    }

    // Live 데이터의 하루 (1주 전 하루)
    public static String getLiveDate() {
        return LocalDate.now().minusDays(7).format(FORMATTER); // 7일 전 하루
    }
}