package com.prgrms.ijuju.domain.chat.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import com.prgrms.ijuju.domain.chat.validation.ValidImage;

import jakarta.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class ChatMessageRequestDTO {
    
    private String roomId;

    private Long senderId;
    
    @Size(max = 1000, message = "메시지는 1000자를 초과할 수 없습니다")
    private String content;

    @ValidImage
    private MultipartFile image;

    private LocalDateTime createdAt;
} 
