package com.prgrms.ijuju.domain.minigame.wordquiz.repository;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.minigame.wordquiz.entity.Difficulty;
import com.prgrms.ijuju.domain.minigame.wordquiz.entity.LimitWordQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LimitWordQuizRepository extends JpaRepository<LimitWordQuiz, Long> {
    List<LimitWordQuiz> findByMember(Member member);

    Optional<LimitWordQuiz> findByMemberAndDifficulty(Member member, Difficulty difficulty);
}
