package com.prgrms.ijuju.domain.article.exception;

import com.prgrms.ijuju.global.exception.BusinessException;

public class ArticleGenerationException extends BusinessException {
    public ArticleGenerationException(ArticleErrorCode errorCode) {
        super(errorCode);
    }

    public ArticleGenerationException() {
        super(ArticleErrorCode.PARSE_FAILURE); // 기본값으로 PARSE_FAILURE를 설정
    }
}