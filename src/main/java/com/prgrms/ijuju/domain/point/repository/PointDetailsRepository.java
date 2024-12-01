package com.prgrms.ijuju.domain.point.repository;

import com.prgrms.ijuju.domain.point.entity.PointType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.prgrms.ijuju.domain.point.entity.PointDetails;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PointDetailsRepository extends JpaRepository<PointDetails, Long> {
    List<PointDetails> findByMemberIdAndCreatedAtBetweenAndPointType(Long memberId, LocalDateTime startDate, LocalDateTime endDate, PointType type);

    @Query("SELECT SUM (p.pointAmount) FROM PointDetails p " +
            "WHERE p.member.id = :memberId AND p.pointStatus = 'EARNED' " +
            "AND p.createdAt >= :startDate AND p.createdAt < :endDate")
    Long calculateEarnedPointsForMember(@Param("memberId") Long memberId,
                                        @Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);
}
