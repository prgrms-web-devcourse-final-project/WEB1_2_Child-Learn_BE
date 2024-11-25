package com.prgrms.ijuju.domain.stock.mid.service;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.stock.mid.dto.response.MidStockPriceResponse;
import com.prgrms.ijuju.domain.stock.mid.dto.response.MidStockResponse;
import com.prgrms.ijuju.domain.stock.mid.dto.response.MidStockTradeInfo;
import com.prgrms.ijuju.domain.stock.mid.dto.response.MidStockWithTradesResponse;
import com.prgrms.ijuju.domain.stock.mid.entity.MidStock;
import com.prgrms.ijuju.domain.stock.mid.entity.MidStockPrice;
import com.prgrms.ijuju.domain.stock.mid.entity.MidStockTrade;
import com.prgrms.ijuju.domain.stock.mid.entity.TradeType;
import com.prgrms.ijuju.domain.stock.mid.repository.MidStockRepository;
import com.prgrms.ijuju.domain.stock.mid.repository.MidStockTradeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MidStockServiceTest {
    @Autowired
    private MemberRepository memberRepository;
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

//    @Test
//    void findStockTrades() {
//        // given
//        Long memberId = 1L;
//        Long midStockId = 1L;
//
//        Member member = memberRepository.save(new Member("1", "test", "test", "test", LocalDate.now(), 1000L, 10L));
//        MidStock midStock = midStockRepository.save(new MidStock("삼성전자"));
//
//        MidStockTrade trade = new MidStockTrade(1L, 100, 1000, TradeType.BUY, midStock, member);
//        midStockTradeRepository.save(trade);
//
//        // when
//        List<MidStockTradeInfo> result = midStockService.findStockTrades(memberId, midStockId);
//        List<MidStockWithTradesResponse> memberStocksAndTrades = midStockService.getMemberStocksAndTrades(memberId);
//
//        // then
//        assertThat(result).hasSize(1);
//        assertThat(memberStocksAndTrades).hasSize(1);
//    }


}