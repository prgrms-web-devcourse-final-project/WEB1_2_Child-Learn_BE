package com.prgrms.ijuju.domain.chat.dto.response;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatReadResponseDTO {
    
    private Long userId;
    private Long roomId;
    private LocalDateTime readAt;
}
