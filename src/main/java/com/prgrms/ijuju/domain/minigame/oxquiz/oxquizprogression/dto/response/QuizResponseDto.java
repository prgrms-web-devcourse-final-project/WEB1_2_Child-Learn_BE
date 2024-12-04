package com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.dto.response;


import com.prgrms.ijuju.domain.minigame.oxquiz.oxquizprogression.constant.Answer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizResponseDto {
    private Long oxQuizDataId;  // 퀴즈 데이터 ID
    private String question;    // 문제 내용
    private String difficulty;  // 난이도
}
