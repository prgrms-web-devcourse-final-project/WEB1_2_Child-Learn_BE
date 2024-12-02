package com.prgrms.ijuju.domain.chat.dto.request;

import lombok.Data;

@Data
public class ChatMessageRequestDTO {
    
    private Long roomId;
    private String content;
    private String imageUrl;
} 
