package com.prgrms.ijuju.point.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.ijuju.point.entity.Point;

import java.util.Optional;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
    Optional<Point> findByMemberId(Long memberId);
}
