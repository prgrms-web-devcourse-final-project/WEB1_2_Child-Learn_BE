package com.prgrms.ijuju.domain.point.controller;

import com.prgrms.ijuju.domain.point.dto.request.PointRequestDTO;
import com.prgrms.ijuju.domain.point.dto.response.PointResponseDTO;
import com.prgrms.ijuju.domain.point.entity.*;
import com.prgrms.ijuju.domain.point.service.PointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PointControllerTest {

    @InjectMocks
    private PointController pointController;

    @Mock
    private PointService pointService;

    @BeforeEach
    void setUp() { // 테스트 전 설정
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCurrentPointsAndCoins() { // 현재 포인트와 코인 조회 테스트
        PointResponseDTO responseDTO = new PointResponseDTO(1000L, 10L);
        when(pointService.CurrentPointsAndCoins(1L)).thenReturn(responseDTO);

        ResponseEntity<PointResponseDTO> response = pointController.getCurrentPointsAndCoins(1L);

        assertEquals(1000L, response.getBody().getCurrentPoints());
        assertEquals(10L, response.getBody().getCurrentCoins());
    }

    @Test
    void testPlayMiniGame() { // 미니게임 테스트
        PointResponseDTO responseDTO = new PointResponseDTO(1100L, 10L);
        when(pointService.playMiniGame(any(Long.class), any(), any(Long.class), any(Boolean.class))).thenReturn(responseDTO);

        PointRequestDTO request = PointRequestDTO.builder()
            .memberId(1L)
            .pointAmount(100L)
            .build();

        ResponseEntity<PointResponseDTO> response = pointController.playMiniGame(request, GameType.CARD_FLIP);

        assertEquals(1100L, response.getBody().getCurrentPoints());
    }

    @Test
    void testExchangePointsToCoins() { // 환전 테스트
        PointResponseDTO responseDTO = new PointResponseDTO(800L, 12L);
        when(pointService.exchangePointsToCoins(any(Long.class), any(Long.class))).thenReturn(responseDTO);

        PointRequestDTO request = PointRequestDTO.builder()
            .memberId(1L)
            .pointAmount(200L)
            .build();   

        ResponseEntity<PointResponseDTO> response = pointController.exchangePointsToCoins(request);

        assertEquals(800L, response.getBody().getCurrentPoints());
        assertEquals(12L, response.getBody().getCurrentCoins());
    }

    @Test
    void testGetFilteredPointHistory() { // 필터링된 포인트 내역 조회 테스트
        List<PointDetails> history = List.of(new PointDetails());
        when(pointService.getFilteredPointHistory(any(Long.class), any(), any(), any())).thenReturn(history);

        ResponseEntity<List<PointDetails>> response = pointController.getFilteredPointHistory(1L, null, null, null);

        assertEquals(1, response.getBody().size());
    }
}
