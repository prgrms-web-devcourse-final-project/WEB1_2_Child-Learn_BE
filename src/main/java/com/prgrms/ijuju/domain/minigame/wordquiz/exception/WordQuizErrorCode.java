package com.prgrms.ijuju.domain.minigame.wordquiz.exception;

import com.prgrms.ijuju.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum WordQuizErrorCode implements ErrorCode {

    MEMBER_NOT_FOUND("WORD_QUIZ_001", "해당 회원이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    INVALID_PLAY_LIMITS("WORD_QUIZ_002", "유효하지 않은 플레이 제한 정보입니다.", HttpStatus.BAD_REQUEST),

    WORD_RETRIEVAL_FAILED("WORD_QUIZ_003", "단어 조회에 실패했습니다.", HttpStatus.NOT_FOUND),
    DAILY_PLAY_LIMIT_EXCEEDED("WORD_QUIZ_004", "오늘의 플레이 횟수를 초과했습니다.", HttpStatus.BAD_REQUEST),
    INVALID_USER("WORD_QUIZ_005", "유효하지 않은 회원정보입니다.", HttpStatus.BAD_REQUEST),

    LIMIT_WORD_QUIZ_NOT_FOUND("WORD_QUIZ_006", "마지막 게임 날짜 조회에 실패했습니다.", HttpStatus.NOT_FOUND),
    GAME_NOT_STARTED("WORD_QUIZ_007", "게임이 시작되지 않았습니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    WordQuizErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.httpStatus = status;
    }
}
