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

@Component
public class TrendAnalyzer {


    //트렌드 계산 메소드
    public List<Trend> analyzeTrends(AdvStock advStock) {
        Map<LocalDate, List<Double>> groupedData = groupByDate(advStock.getTimestamps(), advStock.getClosePrices());

        List<DailyTrend> dailyTrends = calculateDailyTrends(groupedData);

        return calculateCompositeTrends(dailyTrends, advStock.getSymbol());
    }


    // 한시간 단위로 구성되어 있는 FORECAST 데이터를 일단위로 묶어 groupedData 에 저장
    // LocalDate.of(2024, 11, 29): [153.o, 154.5, 155.0] 형식으로 구성 될 것. 아마도. 제발.
    private Map<LocalDate, List<Double>> groupByDate(List<Long> timestamps, List<Double> closePrices) {
        Map<LocalDate, List<Double>> groupedData = new HashMap<>();

        for (int i = 0; i < timestamps.size(); i++) {
            LocalDate date = Instant.ofEpochMilli(timestamps.get(i)).atZone(ZoneId.systemDefault()).toLocalDate();
            groupedData.computeIfAbsent(date, k -> new ArrayList<>()).add(closePrices.get(i));
        }

        return groupedData;
    }

    private List<DailyTrend> calculateDailyTrends(Map<LocalDate, List<Double>> groupedData) {
        List<DailyTrend> dailyTrends = new ArrayList<>();

        for (Map.Entry<LocalDate, List<Double>> entry : groupedData.entrySet()) {
            List<Double> dailyPrices = entry.getValue();
            double startPrice = dailyPrices.get(0);
            double endPrice = dailyPrices.get(dailyPrices.size() - 1);

            //변동률 계산법. 인터넷에서 이렇게 하면 된다는데 맞는 계산법인지 확인 필요
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

    private List<Trend> calculateCompositeTrends(List<DailyTrend> dailyTrends, String symbol) {
        List<Trend> trends = new ArrayList<>();

        trends.add(new Trend("SHORT_TERM", calculateTrendDescription(dailyTrends.subList(0, Math.min(2, dailyTrends.size()))), symbol));
        trends.add(new Trend("MID_TERM", calculateTrendDescription(dailyTrends.subList(0, Math.min(5, dailyTrends.size()))), symbol));
        trends.add(new Trend("LONG_TERM", calculateTrendDescription(dailyTrends), symbol));

        return trends;
    }

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