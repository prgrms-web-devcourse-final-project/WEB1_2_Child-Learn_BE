package com.prgrms.ijuju.domain.avatar.repository;

import com.prgrms.ijuju.domain.avatar.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    Optional<Purchase> findByItemIdAndMemberId(Long itemId, Long memberId);

}
