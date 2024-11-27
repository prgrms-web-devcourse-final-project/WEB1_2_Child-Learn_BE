package com.prgrms.ijuju.domain.ranking.repository;

import com.prgrms.ijuju.domain.ranking.entity.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankingRepository extends JpaRepository<Ranking, Long> {
}
