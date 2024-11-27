package com.prgrms.ijuju.domain.avatar.repository;

import com.prgrms.ijuju.domain.avatar.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

    boolean existsByName(String name);
}
