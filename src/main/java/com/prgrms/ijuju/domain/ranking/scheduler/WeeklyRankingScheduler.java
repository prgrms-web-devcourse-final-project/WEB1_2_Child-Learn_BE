package com.prgrms.ijuju.domain.ranking.scheduler;

import com.prgrms.ijuju.domain.ranking.service.RankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WeeklyRankingScheduler {

    private final RankingService rankingService;

    @Scheduled(cron = "0 0 9 * * MON")
    public void updateWeeklyRanking() {
        log.info("주간 랭킹 초기화(월요일 9시마다)");
        rankingService.updateBulkWeekStart();
    }

    @Scheduled(cron = "0 0 */1 * * *") // 매시간 실행
    public void scheduleRankingUpdate() {
        log.info("랭킹 업데이트 스케줄러 실행(1시간마다)");
        rankingService.updateRanking();
    }
}
