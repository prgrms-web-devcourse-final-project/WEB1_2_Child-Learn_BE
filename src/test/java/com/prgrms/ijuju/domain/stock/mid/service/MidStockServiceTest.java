package com.prgrms.ijuju.domain.stock.mid.service;

import com.prgrms.ijuju.domain.stock.mid.dto.response.MidStockResponse;
import com.prgrms.ijuju.domain.stock.mid.dto.response.MidStockTradeResponse;
import com.prgrms.ijuju.domain.stock.mid.entity.MidStock;
import com.prgrms.ijuju.domain.stock.mid.entity.MidStockTrade;
import com.prgrms.ijuju.domain.stock.mid.entity.TradeType;
import com.prgrms.ijuju.domain.stock.mid.repository.MidStockRepository;
import com.prgrms.ijuju.domain.stock.mid.repository.MidStockTradeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
class MidStockServiceTest {
    @Autowired
    private MidStockRepository midStockRepository;
    @Autowired
    private MidStockTradeRepository midStockTradeRepository;
    @Autowired
    private MidStockService midStockService;

    @Test
    void findAllStocks() {
        // given
        MidStock stock1 = new MidStock("삼성전자");
        MidStock stock2 = new MidStock("현대차");
        midStockRepository.saveAll(Arrays.asList(stock1, stock2));

        // when
        List<MidStockResponse> result = midStockService.findAllStocks();

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).midName()).isEqualTo("삼성전자");
        assertThat(result.get(1).midName()).isEqualTo("현대차");
    }

    @Test
    @DisplayName("주식 거래 내역을 페이징하여 조회한다")
    void findAllStocksTradeWithPaging() {
        // given
        MidStock stock = new MidStock("삼성전자");
        midStockRepository.save(stock);

        // 거래 내역 20개 생성
        for (int i = 0; i < 20; i++) {
            MidStockTrade trade = MidStockTrade.builder()
                    .tradePoint(1000L * i)
                    .pricePerStock(70000L)
                    .tradeType(TradeType.BUY)
                    .midStock(stock)
                    .build();
            midStockTradeRepository.save(trade);
        }

        // when
        Page<MidStockTradeResponse> result = midStockService.findAllStocksTrade(0, 10);

        // then
        assertThat(result.getContent()).hasSize(10); // 페이지 크기
        assertThat(result.getTotalElements()).isEqualTo(20); // 전체 데이터 수
        assertThat(result.getTotalPages()).isEqualTo(2); // 전체 페이지 수
        assertThat(result.getNumber()).isEqualTo(0); // 현재 페이지 번호
        assertThat(result.isFirst()).isTrue(); // 첫 페이지 여부
    }

}