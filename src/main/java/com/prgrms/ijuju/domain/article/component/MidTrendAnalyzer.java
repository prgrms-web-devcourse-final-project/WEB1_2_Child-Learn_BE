package com.prgrms.ijuju.domain.article.component;

import com.prgrms.ijuju.domain.article.data.Trend;
import com.prgrms.ijuju.domain.stock.mid.entity.MidStockPrice;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class MidTrendAnalyzer {

    public List<Trend> analyzeTrends(List<MidStockPrice> prices, String midName) {
        List<Trend> trends = new ArrayList<>();

        // 3일 기준
        trends.add(new Trend("SHORT_TERM", calculateTrend(prices, 3), midName));

        // 1주 기준
        trends.add(new Trend("MID_TERM", calculateTrend(prices, 7), midName));

        // 2주 기준
        trends.add(new Trend("LONG_TERM", calculateTrend(prices, 14), midName));

        return trends;
    }

    private String calculateTrend(List<MidStockPrice> prices, int days) {
        if (prices.size() < days) return "INSUFFICIENT_DATA";

        double startPrice = prices.get(prices.size() - days).getAvgPrice(); // 시작 가격
        double endPrice = prices.get(prices.size() - 1).getAvgPrice();      // 종료 가격
        double percentageChange = ((endPrice - startPrice) / startPrice) * 100;

        if (percentageChange > 2.0) return "UP";
        if (percentageChange < -2.0) return "DOWN";
        return "STABLE";
    }
}