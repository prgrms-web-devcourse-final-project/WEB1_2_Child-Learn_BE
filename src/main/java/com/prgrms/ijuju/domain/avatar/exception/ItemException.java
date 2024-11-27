package com.prgrms.ijuju.domain.avatar.exception;

import com.prgrms.ijuju.domain.member.exception.MemberTaskException;
import org.springframework.http.HttpStatus;

public enum ItemException {

    ITEM_NOT_FOUND("존재하지 않는 아이템입니다.", HttpStatus.NOT_FOUND),
    NOT_ENOUGH_COINS("코인이 부족합니다.", HttpStatus.BAD_REQUEST),
    INAVALID_ITEM_CATEGORY("유효하지 않은 아이템 카테고리입니다.", HttpStatus.BAD_REQUEST),
    ITEM_IS_ALREADY_REGISTED("이미 등록된 아이템입니다", HttpStatus.BAD_REQUEST),
    ITEM_NOT_REGISTERED("아이템 등록에 실패했습니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus status;

    ItemException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public ItemTaskException getItemTaskException(){
        return new ItemTaskException(this.message, this.status.value());
    }
}
