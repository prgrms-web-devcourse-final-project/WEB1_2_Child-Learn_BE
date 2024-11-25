package com.prgrms.ijuju.domain.stock.adv.advancedinvest.repository;

import com.prgrms.ijuju.domain.stock.adv.advancedinvest.entity.AdvancedInvest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdvancedInvestRepository extends JpaRepository<AdvancedInvest, Long> {
    Optional<AdvancedInvest> findByMemberIdAndPlayedToday(Long memberId, boolean playedToday);
}