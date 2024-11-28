package com.prgrms.ijuju.domain.ranking.scheduler;

import com.prgrms.ijuju.domain.ranking.service.RankingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

class WeeklyRankingSchedulerTest {
    @Mock
    private RankingService rankingService;

    @InjectMocks
    private WeeklyRankingScheduler weeklyRankingScheduler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateWeeklyRanking() {
        weeklyRankingScheduler.updateWeeklyRanking();
        verify(rankingService).updateBulkWeekStart();
    }

    @Test
    public void testScheduleRankingUpdate() {
        weeklyRankingScheduler.scheduleRankingUpdate();
        verify(rankingService).updateRanking();
    }
}
