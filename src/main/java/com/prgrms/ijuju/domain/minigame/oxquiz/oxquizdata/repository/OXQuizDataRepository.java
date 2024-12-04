package com.prgrms.ijuju.domain.minigame.oxquiz.oxquizdata.repository;

import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizdata.entity.OXQuizData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OXQuizDataRepository extends JpaRepository<OXQuizData, Long> {
}