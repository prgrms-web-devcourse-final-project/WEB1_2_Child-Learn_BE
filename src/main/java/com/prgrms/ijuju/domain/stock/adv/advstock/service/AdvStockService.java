package com.prgrms.ijuju.domain.stock.adv.advstock.service;

import com.prgrms.ijuju.domain.stock.adv.advstock.constant.DataType;
import com.prgrms.ijuju.domain.stock.adv.advstock.dto.PolygonCandleResponse;
import com.prgrms.ijuju.domain.stock.adv.advstock.entity.AdvStock;

import java.util.List;

/**
 * AdvStockService 에서 getReferenceData 와 getLiveData 는 Controller 와 마찬가지로 순수하게 테스트 용도입니다. 실제 서비스 시 데이터를 가져오는건 AdvStockDataFetcher 클래스가 담당합니다

 * SaveStockData 는 실세 서비스에서 사용이 되는 유일한 메소드 입니다. 간단하게 그냥 데이터를 저장하는 서비스 입니다.

 * deleteByDataType 은 생성은 해두었는데, 실제 사용을 할지에 대해선 미지수 입니다. 이또한 AdvStockScheduler 에서 담당하기 때문입니다. 테스트 할때도 그냥 DB에서 삭제해버리면 되서 일단 서비스만 만들어
 * 두었습니다. 그렇기에 현재 사용이 되지 않는 메소드 입니다. 좀 더 데이터를 크게 해서 테스트시 사용할 수 있어 냅둡니다.
 */
public interface AdvStockService {

    List<AdvStock> getReferenceData();

    AdvStock getLiveData(String symbol, int hour);

    AdvStock saveStockData(String symbol, String name, PolygonCandleResponse response, DataType dataType);

    void deleteByDataType(DataType dataType);
}
