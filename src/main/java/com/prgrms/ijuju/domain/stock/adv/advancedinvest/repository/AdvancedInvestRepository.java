package com.prgrms.ijuju.domain.stock.adv.advancedinvest.repository;


import com.prgrms.ijuju.domain.stock.adv.advancedinvest.entity.AdvancedInvest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AdvancedInvestRepository extends JpaRepository<AdvancedInvest, Long> {

    Optional<AdvancedInvest> findByMemberIdAndPlayedTodayTrue(Long member_id);

    @Modifying
    @Query("UPDATE AdvancedInvest ai SET ai.playedToday = false WHERE ai.playedToday = true")
    void resetPlayedToday();

    @Query("SELECT a FROM AdvancedInvest a WHERE a.paused = true")
    List<AdvancedInvest> findAllByPausedTrue();
}

