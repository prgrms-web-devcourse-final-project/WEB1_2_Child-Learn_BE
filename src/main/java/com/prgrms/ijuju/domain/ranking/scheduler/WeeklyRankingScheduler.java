package com.prgrms.ijuju.domain.ranking.scheduler;

import com.prgrms.ijuju.domain.ranking.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WeeklyRankingScheduler {

    private final RankingService rankingService;

    @Scheduled(cron = "0 0 9 * * MON")
    public void updateWeeklyRanking() {
        rankingService.updateWeekStartAndEnd();
    }
}
