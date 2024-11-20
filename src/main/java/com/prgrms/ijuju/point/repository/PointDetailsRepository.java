package com.prgrms.ijuju.point.repository;

import com.prgrms.ijuju.point.entity.PointDetails;
import com.prgrms.ijuju.point.entity.PointType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointDetailsRepository extends JpaRepository<PointDetails, Long> {
    List<PointDetails> findByMemberIdAndPointType(Long memberId, PointType pointType);
}
