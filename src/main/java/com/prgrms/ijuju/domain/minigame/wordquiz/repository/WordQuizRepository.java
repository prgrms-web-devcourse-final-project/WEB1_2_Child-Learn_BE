package com.prgrms.ijuju.domain.minigame.wordquiz.repository;

import com.prgrms.ijuju.domain.minigame.wordquiz.entity.WordQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordQuizRepository extends JpaRepository<WordQuiz, Long> {
}
