package com.prgrms.ijuju.domain.point.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.prgrms.ijuju.domain.point.entity.GamePoint;

@Repository
public interface GamePointRepository extends JpaRepository<GamePoint, Long> {
} 