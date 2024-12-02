package com.prgrms.ijuju.domain.stock.adv.advancedinvest.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.ijuju.global.util.WebSocketUtil;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.dto.request.StockTransactionRequestDto;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.dto.request.WebSocketRequestDto;
import com.prgrms.ijuju.domain.stock.adv.advancedinvest.service.AdvancedInvestService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class AdvancedInvestWebSocketHandler extends TextWebSocketHandler {
    private final AdvancedInvestService advancedInvestService;
    private final ObjectMapper objectMapper;

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) throws Exception {

        WebSocketRequestDto requestDto = objectMapper.readValue(message.getPayload(), WebSocketRequestDto.class);


        switch (requestDto.getAction()) {
            case "START_GAME":
                advancedInvestService.startGame(session, requestDto.getMemberId());
                WebSocketUtil.send(session, "게임이 시작되었습니다.");
                break;

            case "PAUSE_GAME":
                advancedInvestService.pauseGame(requestDto.getAdvId());
                WebSocketUtil.send(session, "게임이 일시정지되었습니다.");
                break;

            case "RESUME_GAME":
                advancedInvestService.resumeGame(session, requestDto.getAdvId());
                WebSocketUtil.send(session, "게임이 재개되었습니다.");
                break;

            case "END_GAME":
                advancedInvestService.endGame(requestDto.getAdvId());
                WebSocketUtil.send(session, "게임이 종료되었습니다.");
                break;

            case "GET_REMAINING_TIME":
                int remainingTime = advancedInvestService.getRemainingTime(requestDto.getAdvId());
                Map<String, Object> response = new HashMap<>();
                response.put("remainingTime", remainingTime);
                WebSocketUtil.send(session, response); // JSON 형식으로 전송
                break;

            case "GET_VOLUMES":
                advancedInvestService.getRecentVolumes(session, requestDto.getStockSymbol(), requestDto.getAdvId());
                WebSocketUtil.send(session, "거래량 데이터 조회");
                break;


            case "BUY_STOCK":
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
                WebSocketUtil.send(session, "알 수 없는 액션입니다: " + requestDto.getAction());
        }
    }
}
