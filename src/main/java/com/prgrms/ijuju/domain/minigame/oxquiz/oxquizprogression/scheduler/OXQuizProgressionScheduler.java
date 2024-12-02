package com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.scheduler;

import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.constant.Priority;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.repository.OXQuizProgressionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class OXQuizProgressionScheduler {

    private final OXQuizProgressionRepository progressionRepository;

    @Scheduled(cron = "0 0 7 * * ?")
    public void removeOldLowPriority() {
        LocalDate oneWeekAgo = LocalDate.now().minus(1, ChronoUnit.WEEKS);
        long removed = progressionRepository.deleteByPriorityAndAttemptDateBefore(Priority.LOW, oneWeekAgo);
        log.info("Removed {} low-priority progressions older than 2 weeks.", removed);
    }
}