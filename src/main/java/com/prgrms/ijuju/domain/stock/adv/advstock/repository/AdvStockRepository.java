package com.prgrms.ijuju.domain.stock.adv.advstock.repository;

import com.prgrms.ijuju.domain.stock.adv.advstock.entity.AdvStock;
import com.prgrms.ijuju.domain.stock.adv.advstock.constant.DataType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdvStockRepository extends JpaRepository<AdvStock, Long> {

    List<AdvStock> findByDataType(DataType dataType); // Reference 와 Live 기준으로 Stock 검색 (게임 데이터 불러오기 용) > DataType Enum 에 자세히

    Optional<AdvStock> findBySymbolAndDataType(String symbol, DataType dataType); //Symbol 과 DataType 기준으로 검색. 실질적으로 이걸 사용하여 불러올 것

    void deleteByDataType(DataType dataType); //전체 삭제입니다. 애초에 삭제가 자동인지라 사실 필요 없긴 합니다

}
