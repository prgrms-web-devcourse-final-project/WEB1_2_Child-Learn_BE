package com.prgrms.ijuju.domain.minigame.flipcard.repository;

import com.prgrms.ijuju.domain.minigame.flipcard.entity.FlipCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FlipCardRepository extends JpaRepository<FlipCard, Long> {
    @Query(value = "SELECT * FROM flip_card ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<FlipCard> findRandomCards(@Param("count") int count);
}
