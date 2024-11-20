package com.prgrms.ijuju.stock.adv.stock.service;

import com.prgrms.ijuju.stock.adv.stock.constant.DataType;
import com.prgrms.ijuju.stock.adv.stock.dto.PolygonCandleResponse;
import com.prgrms.ijuju.stock.adv.stock.entity.Stock;
import com.prgrms.ijuju.stock.adv.stock.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    public StockServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }


    //레퍼런스 데이터를 받아옵니다. 레퍼런스 데이터는 1주일치 데이터이며, 한번에 업데이트 되기 때문에 builder 없이 들고옵니다.
    //지금 뇌속에서는 작동할거 같은데 테스트 해보고 안되면 builder 이용해서 가져오겠습니다. 근데 데이터 변형이 전혀 필요 없을거 같아서 될듯?
    @Override
    public List<Stock> getReferenceData() {
        return stockRepository.findByDataType(DataType.REFERENCE);
    }


    //라이브 데이터를 들고 옵니다. 일단 간단하게 작성되었으며, 특정 시간과 심볼을 통해 해당 수치를 불러옵니다.
    //hour 은 실제 시간을 뜻합니다. 즉 10 을 입력시 오전 10시를 뜻합니다. 이 경우 10시의 해당 주식 데이터를 불러오게 됩니다
    //UTC 기준입니다!
    @Override
    public Stock getLiveData(String symbol, int hour) {

        if (hour < 14 || hour > 21) {
            throw new IllegalArgumentException(("유효하지 않은 거래 시간. NYSE 거래 시간을 따라가기에 오후 2시 ~ 오후 9시 기준으로 잡혀야 합니다"));
        }
        Stock liveData = stockRepository.findBySymbolAndDataType(symbol, DataType.LIVE)
                .orElseThrow(() -> new IllegalArgumentException("해당 심볼의 라이브 데이터를 찾을 수 없습니다 : " + symbol));

        int index = hour - 14; // 9시 ~ 15시 기준 >> 해외 기준이라 시간 변동 가능성 있음.
        return Stock.builder()
                .symbol(liveData.getSymbol())
                .name(liveData.getName())
                .openPrices(List.of(liveData.getOpenPrices().get(index)))
                .highPrices(List.of(liveData.getHighPrices().get(index)))
                .lowPrices(List.of(liveData.getLowPrices().get(index)))
                .closePrices(List.of(liveData.getClosePrices().get(index)))
                .volumes(List.of(liveData.getVolumes().get(index)))
                .timestamps(List.of(liveData.getTimestamps().get(index)))
                .dataType(DataType.LIVE)
                .build();
    }

    @Override
    public Stock saveStockData(String symbol, String name, PolygonCandleResponse response, DataType dataType) {
        Stock stock = Stock.builder()
                .symbol(symbol)
                .name(name)
                .openPrices(response.getOpenPrices())
                .highPrices(response.getHighPrices())
                .lowPrices(response.getLowPrices())
                .closePrices(response.getClosePrices())
                .volumes(response.getVolumes())
                .timestamps(response.getTimestamps())
                .dataType(dataType)
                .build();

        return stockRepository.save(stock);
    }

    @Override
    public void deleteByDataType(DataType dataType) {
        stockRepository.deleteByDataType(dataType);
    }
}
