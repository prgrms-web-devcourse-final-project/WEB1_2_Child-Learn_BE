package com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizRequestDto {
    private Long memberId;
    private String difficulty; // easy, medium, hard
}