package com.prgrms.ijuju.domain.stock.begin.dto.response;

import com.prgrms.ijuju.domain.stock.begin.entity.BeginQuiz;

public record BeginStockQuizResponse(
        String content,
        String oContent,
        String xContent,
        char answer
) {
    public static BeginStockQuizResponse from(BeginQuiz beginQuiz) {
        return new BeginStockQuizResponse(
                beginQuiz.getContent(),
                beginQuiz.getOContent(),
                beginQuiz.getXContent(),
                beginQuiz.getAnswer()
        );
    }

}
