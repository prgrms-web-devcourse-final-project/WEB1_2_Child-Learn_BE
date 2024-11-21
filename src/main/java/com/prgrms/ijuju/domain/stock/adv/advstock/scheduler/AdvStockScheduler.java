package com.prgrms.ijuju.domain.stock.adv.advstock.scheduler;

import com.prgrms.ijuju.domain.stock.adv.advstock.constant.DataType;
import com.prgrms.ijuju.domain.stock.adv.advstock.dto.PolygonCandleResponse;
import com.prgrms.ijuju.domain.stock.adv.advstock.repository.AdvStockRepository;
import com.prgrms.ijuju.domain.stock.adv.advstock.service.AdvStockDataFetcher;
import com.prgrms.ijuju.domain.stock.adv.advstock.service.AdvStockService;
import com.prgrms.ijuju.domain.stock.adv.advstock.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Stock 엔티티의 알파이자 오메가 입니다
 * Scheduler 를 사용해 자동으로 아침 7시 기준 (한국 시간) 자동으로 실행 됩니다

 * symbol 내부에 있는 주식들의 수치를 가져옵니다. 가져오는 방식은 AdvStockDataFetcher 를 참고
 * 오늘을 20일 이라고 가정했을때 6~12일의 데이터를 Reference Data 로, 13일의 데이터를 Live Data 로 저장합니다

 * 현재 7시 기준으로 기존 데이터를 깔끔하게 날려버립니다. 원래 하루 지우고 하루 생성하고 하는 방식으로 할려고 했지만, 코드의 난잡성과 실제 성능 차이가 거의 안나는걸 확인했습니다.
 * 그렇기에 7시 기준으로 Reference 와 Live 데이터가 한번에 전부 삭제되고, 새로운 Reference 와 Live Data 를 불러옵니다.
 * 보유 주식 에는 영향이 없습니다.
 */

@Component
@RequiredArgsConstructor
public class AdvStockScheduler {

    private final AdvStockDataFetcher advStockDataFetcher;
    private final AdvStockService advStockService;
    private final AdvStockRepository advStockRepository;

    @Scheduled(cron = "0 0 7 * * ?", zone = "Asia/Seoul")
    public void fetchAndUpdateStockDataDaily() {

        //괴거 데이터 삭제용. 7시 리셋시 모든 데이터를 날린다
        advStockRepository.deleteByDataType(DataType.REFERENCE);
        advStockRepository.deleteByDataType(DataType.LIVE);


        //테스트 용으로 일단 5개만 넣어뒀습니다.
        String[] symbols = {"AAPL", "MSFT", "GOOGL", "AMZN", "TSLA"};

        String referenceStartDate = DateUtil.getReferenceStartDate();
        String referenceEndDate = DateUtil.getReferenceEndDate();
        String liveDate = DateUtil.getLiveDate();

        for (String symbol : symbols) {
            PolygonCandleResponse referenceData = advStockDataFetcher.fetchStockData(symbol, 1, "hour", referenceStartDate, referenceEndDate);
            advStockService.saveStockData(symbol, symbol + " Name", referenceData, DataType.REFERENCE);

            // Live 데이터 저장
            PolygonCandleResponse liveData = advStockDataFetcher.fetchStockData(symbol, 1, "hour", liveDate, liveDate);
            advStockService.saveStockData(symbol, symbol + " Name", liveData, DataType.LIVE);
        }

    }
}