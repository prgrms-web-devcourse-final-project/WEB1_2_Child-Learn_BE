package com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.repository;

import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.constant.Priority;
import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.entity.OXQuizProgression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OXQuizProgressionRepository extends JpaRepository<OXQuizProgression, Long> {

    List<OXQuizProgression> findByMemberId(Long memberId);

    long deleteByPriorityAndAttemptDateBefore(Priority priority, LocalDate date);
}