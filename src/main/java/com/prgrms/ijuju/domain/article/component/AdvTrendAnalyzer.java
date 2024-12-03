package com.prgrms.ijuju.domain.article.component;

import com.prgrms.ijuju.domain.article.data.DailyTrend;
import com.prgrms.ijuju.domain.article.data.Trend;
import com.prgrms.ijuju.domain.stock.adv.advstock.entity.AdvStock;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Adv 같은 경우는 timeStamp 로 이루어져 있으며, 한시간 봉으로 데이터가 이루어져 있습니다
 * 데이터 분석을 위해서 모든 데이터를 전부 "하루" 단위로 변경합니다
 */
@Component
public class AdvTrendAnalyzer {

    // 트렌드 계산 메소드
    public List<Trend> analyzeTrends(AdvStock advStock) {
        Map<LocalDate, List<Double>> groupedData = groupByDate(advStock.getTimestamps(), advStock.getClosePrices());

        List<DailyTrend> dailyTrends = calculateDailyTrends(groupedData);

        return calculateCompositeTrends(dailyTrends, advStock.getSymbol());
    }

    // 한 시간 단위 데이터를 일 단위로 묶음
    private Map<LocalDate, List<Double>> groupByDate(List<Long> timestamps, List<Double> closePrices) {
        Map<LocalDate, List<Double>> groupedData = new HashMap<>();

        for (int i = 0; i < timestamps.size(); i++) {
            LocalDate date = Instant.ofEpochMilli(timestamps.get(i)).atZone(ZoneId.systemDefault()).toLocalDate();
            groupedData.computeIfAbsent(date, k -> new ArrayList<>()).add(closePrices.get(i));
        }

        return groupedData;
    }

    // DailyTrend 계산
    private List<DailyTrend> calculateDailyTrends(Map<LocalDate, List<Double>> groupedData) {
        List<DailyTrend> dailyTrends = new ArrayList<>();

        for (Map.Entry<LocalDate, List<Double>> entry : groupedData.entrySet()) {
            List<Double> dailyPrices = entry.getValue();
            double startPrice = dailyPrices.get(0);
            double endPrice = dailyPrices.get(dailyPrices.size() - 1);

            double percentageChange = ((endPrice - startPrice) / startPrice) * 100;
            String trendType = determineTrendType(percentageChange);

            dailyTrends.add(new DailyTrend(entry.getKey(), trendType, percentageChange));
        }

        return dailyTrends;
    }

    private String determineTrendType(double percentageChange) {
        if (percentageChange > 2.0) return "UP";
        if (percentageChange < -2.0) return "DOWN";
        return "STABLE";
    }

    // Composite Trends 계산
    private List<Trend> calculateCompositeTrends(List<DailyTrend> dailyTrends, String symbol) {
        List<Trend> trends = new ArrayList<>();

        // Short Term (3일)
        if (dailyTrends.size() >= 3) {
            trends.add(new Trend("SHORT_TERM",
                    calculateTrendDescription(dailyTrends.subList(0, Math.min(3, dailyTrends.size()))), symbol));
        }

        // Mid Term (7일)
        if (dailyTrends.size() >= 7) {
            trends.add(new Trend("MID_TERM",
                    calculateTrendDescription(dailyTrends.subList(0, Math.min(7, dailyTrends.size()))), symbol));
        }

        // Long Term (14일)
        trends.add(new Trend("LONG_TERM", calculateTrendDescription(dailyTrends), symbol));

        return trends;
    }

    // 트렌드 설명 생성
    private String calculateTrendDescription(List<DailyTrend> dailyTrends) {
        StringBuilder description = new StringBuilder();
        String currentTrend = null;
        int duration = 0;

        for (DailyTrend dailyTrend : dailyTrends) {
            if (!dailyTrend.getTrendType().equals(currentTrend)) {
                if (currentTrend != null) {
                    description.append(currentTrend).append(": ").append(duration).append("일, ");
                }
                currentTrend = dailyTrend.getTrendType();
                duration = 1;
            } else {
                duration++;
            }
        }

        if (currentTrend != null) {
            description.append(currentTrend).append(": ").append(duration).append("일");
        }

        return description.toString();
    }
}