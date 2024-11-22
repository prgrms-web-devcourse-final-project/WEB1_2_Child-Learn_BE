package com.prgrms.ijuju.domain.point.repository;

import org.springframework.stereotype.Repository;
import com.prgrms.ijuju.domain.point.entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Repository
public interface CoinRepository extends JpaRepository<Coin, Long> {
    Optional<Coin> findByMemberId(Long memberId);
    @Modifying
    @Query("UPDATE Coin c SET c.currentCoins = :currentCoins WHERE c.member.id = :memberId")
    void updateCurrentCoins(@Param("memberId") Long memberId, @Param("currentCoins") Long currentCoins);
} 