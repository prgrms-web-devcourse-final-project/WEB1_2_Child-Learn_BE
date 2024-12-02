package com.prgrms.ijuju.domain.avatar.repository;

import com.prgrms.ijuju.domain.avatar.entity.Item;
import com.prgrms.ijuju.domain.avatar.entity.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    boolean existsByName(String name);

    List<Item> findAllByCategory(ItemCategory category);
}
