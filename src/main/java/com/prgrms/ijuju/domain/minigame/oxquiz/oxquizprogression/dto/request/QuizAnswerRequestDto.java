package com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizAnswerRequestDto {
    private String userAnswer; // "O" or "X"
}