package com.prgrms.ijuju.domain.point.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prgrms.ijuju.domain.point.entity.CoinDetails;

import java.util.List;

@Repository
public interface CoinDetailsRepository extends JpaRepository<CoinDetails, Long> {
    List<CoinDetails> findByMemberId(Long memberId);
}
