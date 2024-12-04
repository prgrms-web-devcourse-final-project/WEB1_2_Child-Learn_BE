package com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizResponseDto {
    private Long progressionId; // 진행 상태 ID
    private Long oxQuizDataId;  // 퀴즈 데이터 ID
    private String question;    // 문제 내용
    private String explanation; // 문제 설명
    private String difficulty;  // 난이도
    private boolean isCorrect;  // 유저가 맞췄는지 여부


    public QuizResponseDto(Long progressionId, Long oxQuizDataId, String explanation, boolean isCorrect) {
        this.progressionId = progressionId;
        this.oxQuizDataId = oxQuizDataId;
        this.explanation = explanation;
        this.isCorrect = isCorrect;
    }
}