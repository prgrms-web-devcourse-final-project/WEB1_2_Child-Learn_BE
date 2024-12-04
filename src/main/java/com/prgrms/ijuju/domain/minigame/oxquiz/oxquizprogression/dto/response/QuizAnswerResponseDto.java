package com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizAnswerResponseDto {
    private Long oxQuizDataId;  // 퀴즈 데이터 ID
    private String explanation; // 설명
    private boolean isCorrect;  // 정답 여부
}