package com.prgrms.ijuju.domain.point.service;

import com.prgrms.ijuju.domain.member.entity.Member;
import com.prgrms.ijuju.domain.member.repository.MemberRepository;
import com.prgrms.ijuju.domain.point.dto.request.PointRequestDTO;
import com.prgrms.ijuju.domain.point.dto.response.PointResponseDTO;
import com.prgrms.ijuju.domain.point.entity.ExchangeDetails;
import com.prgrms.ijuju.domain.point.entity.GameType;
import com.prgrms.ijuju.domain.point.entity.PointDetails;
import com.prgrms.ijuju.domain.point.entity.StockStatus;
import com.prgrms.ijuju.domain.point.entity.StockType;
import com.prgrms.ijuju.domain.point.repository.ExchangeRepository;
import com.prgrms.ijuju.domain.point.repository.PointDetailsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PointServiceTest {

    @InjectMocks
    private PointService pointService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PointDetailsRepository pointDetailsRepository;

    @Mock
    private ExchangeRepository exchangeRepository;

    private Member member;
    
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        member = Member.builder()
//            .loginId("testLoginId")
//            .pw("testPw")
//            .username("testUsername")
//            .email("testEmail")
//            .birth(LocalDate.now())
//            .points(1000L)
//            .coins(10L)
//            .build();
//        member.setId(1L);
//    }

    @Test
    @DisplayName("현재 포인트와 코인 조회 테스트")
    void testCurrentPointsAndCoins() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        PointResponseDTO response = pointService.CurrentPointsAndCoins(1L);

        assertEquals(1000L, response.getCurrentPoints());
        assertEquals(10L, response.getCurrentCoins());
    }

    @Test
    @DisplayName("미니게임 테스트")
    void testPlayMiniGame() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        PointResponseDTO response = pointService.playMiniGame(1L, GameType.CARD_FLIP, 100L, true);

        assertEquals(1100L, response.getCurrentPoints());
        verify(pointDetailsRepository, times(1)).save(any(PointDetails.class));
    }

    @Test
    @DisplayName("주식 테스트")
    void testSimulateStockInvestment() {
        PointRequestDTO request = PointRequestDTO.builder()
            .memberId(1L)
            .pointAmount(200L)
            .build();

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        PointResponseDTO response = pointService.simulateStockInvestment(request, StockType.BEGIN, StockStatus.BUY);

        assertEquals(800L, response.getCurrentPoints());
        verify(pointDetailsRepository, times(1)).save(any(PointDetails.class));
    }

    @Test
    @DisplayName("환전 테스트")
    void testExchangePointsToCoins() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        PointResponseDTO response = pointService.exchangePointsToCoins(1L, 200L);

        assertEquals(800L, response.getCurrentPoints());
        assertEquals(12L, response.getCurrentCoins());
        verify(pointDetailsRepository, times(1)).save(any(PointDetails.class));
        verify(exchangeRepository, times(1)).save(any(ExchangeDetails.class));
    }
}
