package com.prgrms.ijuju.domain.article.component;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrendAnalyzer {

    public String analyzeTrend(List<Double> closePrices) {
        if (closePrices == null || closePrices.isEmpty()) {
            throw new IllegalArgumentException("종가 데이터가 없습니다.");
        }

        double firstPrice = closePrices.get(0);
        double lastPrice = closePrices.get(closePrices.size() - 1);

        double percentageChange = ((lastPrice - firstPrice) / firstPrice) * 100;

        if (percentageChange > 2.0) {
            return "UP"; // 2% 이상 상승
        } else if (percentageChange < -2.0) {
            return "DOWN"; // 2% 이상 하락
        } else {
            return "STABLE"; // -2% ~ 2% 사이
        }
    }
}