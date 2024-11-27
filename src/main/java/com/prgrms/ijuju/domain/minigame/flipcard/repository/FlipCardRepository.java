package com.prgrms.ijuju.domain.minigame.flipcard.repository;

import com.prgrms.ijuju.domain.minigame.flipcard.entity.FlipCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlipCardRepository extends JpaRepository<FlipCard, Long> {
}
