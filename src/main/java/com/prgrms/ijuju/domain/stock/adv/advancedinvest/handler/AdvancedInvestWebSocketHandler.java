package com.prgrms.ijuju.domain.stock.adv.advancedinvest.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.ijuju.global.util.WebSocketUtil;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.dto.request.StockTransactionRequestDto;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.dto.request.WebSocketRequestDto;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.service.AdvancedInvestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdvancedInvestWebSocketHandler extends TextWebSocketHandler {
    private final AdvancedInvestService advancedInvestService;
    private final ObjectMapper objectMapper;

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) throws Exception {
        log.info("WebSocket 메시지 수신: {}", message.getPayload());
        WebSocketRequestDto requestDto;
        try {

            requestDto = objectMapper.readValue(message.getPayload(), WebSocketRequestDto.class);
            log.info("파싱된 요청 DTO: {}", requestDto);

            if (requestDto.getAction() == null) {
                log.error("잘못된 요청: action 필드가 누락되었습니다. 요청 DTO: {}", requestDto);
                return;
            }


            switch (requestDto.getAction()) {
                case "START_GAME":
                    validateStartGameRequest(requestDto);
                    advancedInvestService.startGame(session, requestDto.getMemberId());
                    WebSocketUtil.send(session, "게임이 시작되었습니다.");
                    break;

                case "PAUSE_GAME":
                    validateAdvId(requestDto.getAdvId());
                    advancedInvestService.pauseGame(requestDto.getAdvId());
                    WebSocketUtil.send(session, "게임이 일시정지되었습니다.");
                    break;

                case "RESUME_GAME":
                    validateAdvId(requestDto.getAdvId());
                    advancedInvestService.resumeGame(session, requestDto.getAdvId());
                    WebSocketUtil.send(session, "게임이 재개되었습니다.");
                    break;

                case "END_GAME":
                    validateAdvId(requestDto.getAdvId());
                    advancedInvestService.endGame(requestDto.getAdvId());
                    WebSocketUtil.send(session, "게임이 종료되었습니다.");
                    break;

                case "GET_REMAINING_TIME":
                    validateAdvId(requestDto.getAdvId());
                    int remainingTime = advancedInvestService.getRemainingTime(requestDto.getAdvId());
                    Map<String, Object> response = new HashMap<>();
                    response.put("remainingTime", remainingTime);
                    WebSocketUtil.send(session, response);
                    break;

                case "GET_VOLUMES":
                    validateStockSymbol(requestDto.getStockSymbol());
                    validateAdvId(requestDto.getAdvId());
                    advancedInvestService.getRecentVolumes(session, requestDto.getStockSymbol(), requestDto.getAdvId());
                    WebSocketUtil.send(session, "거래량 데이터 조회");
                    break;

                case "BUY_STOCK":
                    validateBuyOrSellRequest(requestDto);
                    StockTransactionRequestDto buyRequest = StockTransactionRequestDto.builder()
                            .stockSymbol(requestDto.getStockSymbol())
                            .quantity(requestDto.getQuantity())
                            .points(requestDto.getPoints())
                            .memberId(requestDto.getMemberId())
                            .build();
                    advancedInvestService.buyStock(requestDto.getAdvId(), buyRequest);
                    WebSocketUtil.send(session, "주식 구매가 완료되었습니다.");
                    break;

                case "SELL_STOCK":
                    validateBuyOrSellRequest(requestDto);
                    StockTransactionRequestDto sellRequest = StockTransactionRequestDto.builder()
                            .stockSymbol(requestDto.getStockSymbol())
                            .quantity(requestDto.getQuantity())
                            .points(requestDto.getPoints())
                            .memberId(requestDto.getMemberId())
                            .build();
                    advancedInvestService.sellStock(requestDto.getAdvId(), sellRequest);
                    WebSocketUtil.send(session, "주식 판매가 완료되었습니다.");
                    break;

                default:
                    log.warn("알 수 없는 액션: {}", requestDto.getAction());
                    WebSocketUtil.send(session, "알 수 없는 액션입니다: " + requestDto.getAction());
            }
        } catch (IllegalArgumentException e) {
            log.error("유효하지 않은 요청 처리 중 에러 발생: {}", e.getMessage(), e);
            WebSocketUtil.send(session, "요청이 유효하지 않습니다: " + e.getMessage());
        } catch (Exception e) {
            log.error("WebSocket 메시지 처리 중 예외 발생: {}", e.getMessage(), e);
            WebSocketUtil.send(session, "서버에서 에러가 발생했습니다. 다시 시도해주세요.");
        }
    }


    // 유효성 검사 메서드들
    private void validateStartGameRequest(WebSocketRequestDto requestDto) {
        if (requestDto.getMemberId() == null) {
            throw new IllegalArgumentException("MemberId가 누락되었습니다.");
        }
    }

    private void validateAdvId(Long advId) {
        if (advId == null) {
            throw new IllegalArgumentException("AdvId가 누락되었습니다.");
        }
    }

    private void validateStockSymbol(String stockSymbol) {
        if (stockSymbol == null || stockSymbol.isEmpty()) {
            throw new IllegalArgumentException("StockSymbol이 누락되었습니다.");
        }
    }

    private void validateBuyOrSellRequest(WebSocketRequestDto requestDto) {
        validateAdvId(requestDto.getAdvId());
        validateStockSymbol(requestDto.getStockSymbol());
        if (requestDto.getQuantity() <= 0) {
            throw new IllegalArgumentException("수량은 0보다 커야 합니다.");
        }
        if (requestDto.getPoints() == null || requestDto.getPoints().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("유효한 포인트 값이 필요합니다.");
        }
    }
}


