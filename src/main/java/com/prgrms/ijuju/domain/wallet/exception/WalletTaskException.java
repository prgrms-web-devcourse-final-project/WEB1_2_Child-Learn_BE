package com.prgrms.ijuju.domain.wallet.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter 
public class WalletTaskException extends RuntimeException {
    
    private final String code;
    private final String message;
    private final int statusCode;
    private final HttpStatus httpStatus;

    public WalletTaskException(String code, String message, int statusCode) {
        super(message);
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
        this.httpStatus = HttpStatus.valueOf(statusCode);
    }
} 
