package com.prgrms.ijuju.global.exception;

import com.prgrms.ijuju.domain.wallet.exception.WalletException;
import com.prgrms.ijuju.global.common.dto.ErrorResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("CustomException: {}", e.getMessage());
        WalletException pointException = e.getPointException();
        
        ErrorResponse response = ErrorResponse.builder()
            .code(pointException.getCode())
            .message(pointException.getMessage())
            .status(pointException.getHttpStatus().value())
            .build();
            
        return new ResponseEntity<>(response, pointException.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unexpected error: {}", e.getMessage());
        
        ErrorResponse response = ErrorResponse.builder()
            .code("SERVER_ERROR")
            .message("서버 오류가 발생했습니다.")
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .build();
            
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
} 