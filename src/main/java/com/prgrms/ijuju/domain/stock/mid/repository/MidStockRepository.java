package com.prgrms.ijuju.domain.stock.mid.repository;

import com.prgrms.ijuju.domain.stock.mid.entity.MidStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MidStockRepository extends JpaRepository<MidStock, Long> {

    @Query("SELECT m.id FROM MidStock m")
    List<Long> findAllIds();
}
