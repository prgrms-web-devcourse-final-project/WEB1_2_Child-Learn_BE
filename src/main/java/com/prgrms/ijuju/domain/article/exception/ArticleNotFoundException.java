package com.prgrms.ijuju.domain.article.exception;

import com.prgrms.ijuju.global.exception.BusinessException;

public class ArticleNotFoundException extends BusinessException {
    public ArticleNotFoundException(ArticleErrorCode errorCode) {
        super(errorCode);
    }
}