package com.prgrms.ijuju.domain.point.repository;

import org.springframework.stereotype.Repository;

import com.prgrms.ijuju.domain.point.entity.Point;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
    Optional<Point> findByMemberId(Long memberId);

    @Modifying
    @Query("UPDATE Point p SET p.currentPoints = :currentPoints WHERE p.member.id = :memberId")
    void updateCurrentPoints(@Param("memberId") Long memberId, @Param("currentPoints") Long currentPoints);
}
