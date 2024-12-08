package com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.repository;

import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.entity.OXQuizProgression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OXQuizProgressionRepository extends JpaRepository<OXQuizProgression, Long> {
    Optional<OXQuizProgression> findByMemberId(Long memberId);
}