package com.prgrms.ijuju.domain.minigame.wordquiz.repository;

import com.prgrms.ijuju.domain.minigame.wordquiz.entity.WordQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WordQuizRepository extends JpaRepository<WordQuiz, Long> {
    @Query(value = "SELECT * FROM word_quiz ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<WordQuiz> findRandomWord();
}
