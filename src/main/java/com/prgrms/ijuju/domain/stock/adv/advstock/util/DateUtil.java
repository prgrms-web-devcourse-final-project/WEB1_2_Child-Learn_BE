package com.prgrms.ijuju.domain.stock.adv.advstock.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String getReferenceStartDate() {
        return adjustToWeekday(LocalDate.now().minusWeeks(4)).format(FORMATTER);
    }

    public static String getReferenceEndDate() {
        return adjustToWeekday(LocalDate.now().minusWeeks(3).minusDays(1)).format(FORMATTER);
    }

    public static String getLiveDate() {
        return adjustToWeekday(LocalDate.now().minusWeeks(3)).format(FORMATTER);
    }

    public static String getForecastStartDate() {
        return adjustToWeekday(LocalDate.now().minusWeeks(3).plusDays(1)).format(FORMATTER);
    }

    public static String getForecastEndDate() {
        return adjustToWeekday(LocalDate.now().minusDays(1)).format(FORMATTER);
    }

    private static LocalDate adjustToWeekday(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY) {
            return date.minusDays(1); // 금요일로 이동
        } else if (dayOfWeek == DayOfWeek.SUNDAY) {
            return date.minusDays(2); // 금요일로 이동
        }
        return date; // 평일이면 그대로 반환
    }
}