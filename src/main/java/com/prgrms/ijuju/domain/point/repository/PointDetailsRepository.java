package com.prgrms.ijuju.domain.point.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prgrms.ijuju.domain.point.entity.PointDetails;
import com.prgrms.ijuju.domain.point.entity.PointType;
import com.prgrms.ijuju.domain.point.entity.PointStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PointDetailsRepository extends JpaRepository<PointDetails, Long> {
    List<PointDetails> findByMemberIdAndPointTypeAndPointStatus(Long memberId, PointType pointType, PointStatus pointStatus);
    List<PointDetails> findByMemberId(Long memberId);
    List<PointDetails> findByMemberIdAndCreatedAtBetween(Long memberId, LocalDateTime startDate, LocalDateTime endDate);
}
