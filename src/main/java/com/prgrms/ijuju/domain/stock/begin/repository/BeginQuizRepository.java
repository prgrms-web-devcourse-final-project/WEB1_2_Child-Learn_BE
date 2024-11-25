package com.prgrms.ijuju.domain.stock.begin.repository;

import com.prgrms.ijuju.domain.stock.begin.entity.BeginQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BeginQuizRepository extends JpaRepository<BeginQuiz, Long> {
    Optional<List<BeginQuiz>> findByCreatedDate(LocalDate createdDate);
}
