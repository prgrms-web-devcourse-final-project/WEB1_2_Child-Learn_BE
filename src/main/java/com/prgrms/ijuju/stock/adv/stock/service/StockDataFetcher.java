package com.prgrms.ijuju.stock.adv.stock.service;

import com.prgrms.ijuju.stock.adv.stock.dto.PolygonCandleResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


/**
* 해당 클래스는 stock data 를 실제로 들고오는 서비스 클래스 입니다

* 현재 API key 가 상수로 적혀있고 비어있습니다. 테스트 용도고 Advanced_Invest 엔티티 구현 후 환경변수로 설정하여 보안에 신경 쓸 예정입니다.

* url format 같은 경우 symbol 로 저희가 원하는 주식을 들고오고, resolution 을 60으로 설정하여 1시간 단위의 데이터를 들고오게 설정하였습니다.

* RestClient 의 경우 객체로 간단하게 api 를 호촐할 수 있는 Sping 내장 클래스 입니다. Template 상위호환이기에 썼습니다.
* 자세한 사용 방식은 > https://docs.spring.io/spring-framework/reference/integration/rest-clients.html
 */

@Service
public class StockDataFetcher {

    private final RestClient restClient = RestClient.builder().build();
    private static final String POLYGON_API_KEY = " ";  //보안상 이슈로 삭제. 나중에 properties 로 옮기고 환경변수 설정 예정
    private static final String BASE_URL = "https://api.polygon.io/v2/aggs/ticker/%s/range/%d/%s/%s/%s";

    public PolygonCandleResponse fetchStockData(String symbol, int multiplier, String time, String startDate, String endDate) {
        String url = String.format(
                BASE_URL + "?adjusted=true&sort=asc&apiKey=%s",
                symbol, multiplier, time, startDate, endDate, POLYGON_API_KEY
        );


        return restClient.get()
                .uri(url)
                .retrieve()
                .body(PolygonCandleResponse.class);
    }
}
