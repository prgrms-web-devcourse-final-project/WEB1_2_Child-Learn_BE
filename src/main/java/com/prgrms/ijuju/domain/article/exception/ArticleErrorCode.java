package com.prgrms.ijuju.domain.article.exception;

import com.prgrms.ijuju.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ArticleErrorCode implements ErrorCode {
    ARTICLE_NOT_FOUND("ARTICLE_001", "해당 기사를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NO_TRENDS_FOUND("ARTICLE_002", "트렌드를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NO_RESPONSE_CHOICES("ARTICLE_003", "GPT 응답에 choices 데이터가 없습니다.", HttpStatus.BAD_REQUEST),
    PARSE_FAILURE("ARTICLE_004", "GPT 응답 파싱 실패.", HttpStatus.BAD_REQUEST),
    UNKNOWN_TREND_TYPE("ARTICLE_005", "알 수 없는 트렌드 타입입니다.", HttpStatus.BAD_REQUEST),
    MID_STOCK_NOT_FOUND("ARTICLE_006", "해당 MidStock을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ArticleErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}