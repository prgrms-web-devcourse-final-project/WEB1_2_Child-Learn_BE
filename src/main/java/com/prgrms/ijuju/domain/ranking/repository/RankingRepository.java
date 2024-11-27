package com.prgrms.ijuju.domain.ranking.repository;

import com.prgrms.ijuju.domain.ranking.entity.Ranking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RankingRepository extends JpaRepository<Ranking, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Ranking r SET r.weekStart = :weekStart, r.weekEnd = :weekEnd " +
            "WHERE r.weekStart != :weekStart OR r.weekEnd != :weekEnd")
    void updateWeekStartAndEnd(@Param("weekStart") LocalDateTime weekStart, @Param("weekEnd") LocalDateTime weekEnd);

    Optional<Ranking> findByMemberId(Long memberId);

    Page<Ranking> findAllByOrderByWeeklyPointsDesc(Pageable pageable);

    // 특정 회원의 순위 조회를 위한 메서드
    @Query("SELECT COUNT(r) + 1 FROM Ranking r " +
            "WHERE r.weeklyPoints > (SELECT r2.weeklyPoints FROM Ranking r2 WHERE r2.member.id = :memberId)")
    Long findRankByMemberId(@Param("memberId") Long memberId);

    // 친구의 랭킹 조회를 위한 메서드
    Page<Ranking> findAllByMemberIdInOrderByWeeklyPointsDesc(List<Long> friendIds, Pageable pageable);
}
