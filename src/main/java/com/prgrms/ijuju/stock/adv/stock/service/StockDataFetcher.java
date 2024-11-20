package com.prgrms.ijuju.stock.adv.stock.service;

import com.prgrms.ijuju.stock.adv.stock.dto.FinnhubCandleResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
* 해당 클래스는 stock data 를 실제로 들고오는 서비스 클래스 입니다

* 현재 API key 가 상수로 적혀있고 비어있습니다. 테스트 용도고 Advanced_Invest 엔티티 구현 후 환경변수로 설정하여 보안에 신경 쓸 예정입니다.

* url format 같은 경우 symbol 로 저희가 원하는 주식을 들고오고, resolution 을 60으로 설정하여 1시간 단위의 데이터를 들고오게 설정하였습니다.

* RestTemplate 의 경우 객체로 간단하게 api 를 호촐할 수 있는 Sping 내장 클래스 입니다. JSON 을 받을 수 있기에 쓰였습니다.
* 자세한 사용 방식은 > https://blog.naver.com/hj_kim97/222295259904
 */

@Service
public class StockDataFetcher {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String FINNHUB_API_KEY = " ";  //보안상 이슈로 삭제. 나중에 properties 로 옮기고 환경변수 설정 예정
    private static final String FINNHUB_URL = "https://finnhub.io/api/v1/stock/candle";

    public FinnhubCandleResponse fetchStockData(String symbol, long from, long to) {
        String url = String.format(
                    "%s?symbol=%s&resolution=60&from=%d&to=%d&token=%s",
                    FINNHUB_URL, symbol, from, to, FINNHUB_API_KEY
        );

        return restTemplate.getForObject(url, FinnhubCandleResponse.class);
    }
}
