package com.prgrms.ijuju.domain.point.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prgrms.ijuju.domain.point.entity.ExchangeDetails;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExchangeRepository extends JpaRepository<ExchangeDetails, Long> {
    List<ExchangeDetails> findByMemberIdAndCreatedAtBetween(Long memberId, LocalDateTime startDate, LocalDateTime endDate);

    List<ExchangeDetails> findByMemberId(Long memberId);
}
