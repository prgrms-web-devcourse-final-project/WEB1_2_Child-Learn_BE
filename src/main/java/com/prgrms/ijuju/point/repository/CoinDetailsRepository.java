package com.prgrms.ijuju.point.repository;

import com.prgrms.ijuju.point.entity.CoinDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoinDetailsRepository extends JpaRepository<CoinDetails, Long> {
    List<CoinDetails> findByMemberId(Long memberId);
}
