package com.prgrms.ijuju.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class WebSocketUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 객체를 JSON 으로 변환하는 send 메소드
    // AdvancedInvest 용. 혹시 채팅 만들때 쓰면 작동 안할 가능성 200%. 혹시 다른 send 를 만들어야 하면 말해주세요 메소드 이름 바꾸겠습니다
    public static void send(WebSocketSession session, Object data) {
        try {
            if (session.isOpen()) {

                String jsonData = objectMapper.writeValueAsString(data);

                session.sendMessage(new TextMessage(jsonData));
            } else {
                System.out.println("WebSocket 세션 없음");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("메시지 전송 실패", e);
        }
    }
}