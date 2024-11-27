package com.prgrms.ijuju.domain.stock.begin.dto.request;

import java.util.List;

public record ChatGptRequest(
        String model,
        List<BeginChatGptMessage> messages,
        double temperature
) {

}
