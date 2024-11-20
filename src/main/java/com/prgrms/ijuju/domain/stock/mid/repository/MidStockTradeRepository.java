package com.prgrms.ijuju.domain.stock.mid.repository;

import com.prgrms.ijuju.domain.stock.mid.entity.MidStockTrade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MidStockTradeRepository extends JpaRepository<MidStockTrade, Long> {
    @Query("SELECT t FROM MidStockTrade t JOIN FETCH t.midStock")
    Page<MidStockTrade> findAllWithMidStock(Pageable pageable);
}
