package com.prgrms.ijuju.domain.wallet.repository;

import com.prgrms.ijuju.domain.wallet.entity.MiniGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.prgrms.ijuju.domain.wallet.entity.GameType;

import java.util.List;

@Repository
public interface MiniGameRepository extends JpaRepository<MiniGame, Long> {
    List<MiniGame> findByMemberIdOrderByCreatedAtDesc(Long memberId);
    List<MiniGame> findByMemberIdAndGameType(Long memberId, GameType gameType);
} 