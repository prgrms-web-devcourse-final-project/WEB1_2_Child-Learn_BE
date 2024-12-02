package com.prgrms.ijuju.domain.chat.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class ChatReadResponseDTO {
    
    private Long userId;
    private Long roomId;
}
