package com.prgrms.ijuju.domain.article.component;

import com.prgrms.ijuju.domain.article.data.Trend;
import com.prgrms.ijuju.domain.stock.adv.advstock.entity.AdvStock;

import java.util.List;

public interface TrendAnalyzer {
    List<Trend> analyzeTrends(AdvStock advStock);
}
