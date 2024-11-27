package com.prgrms.ijuju.domain.stock.adv.advstock.repository;

import com.prgrms.ijuju.domain.stock.adv.advstock.entity.AdvStock;
import com.prgrms.ijuju.domain.stock.adv.advstock.constant.DataType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdvStockRepository extends JpaRepository<AdvStock, Long> {

    List<AdvStock> findByDataType(DataType dataType);

    Optional<AdvStock> findBySymbolAndDataType(String symbol, DataType dataType);

    void deleteByDataType(DataType dataType);

    Optional<AdvStock> findBySymbol(String symbol);

}
