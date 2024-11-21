package com.prgrms.ijuju.domain.stock.mid.service;

import com.prgrms.ijuju.domain.stock.mid.dto.response.MidStockResponse;
import com.prgrms.ijuju.domain.stock.mid.entity.MidStock;
import com.prgrms.ijuju.domain.stock.mid.repository.MidStockRepository;
import com.prgrms.ijuju.domain.stock.mid.repository.MidStockTradeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
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


}