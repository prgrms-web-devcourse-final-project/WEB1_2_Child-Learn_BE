package com.prgrms.ijuju.global.exception;

import com.prgrms.ijuju.domain.avatar.dto.response.ErrorAvatarResponse;
import com.prgrms.ijuju.domain.avatar.dto.response.ErrorItemResponse;
import com.prgrms.ijuju.domain.avatar.exception.AvatarErrorCode;
import com.prgrms.ijuju.domain.avatar.exception.AvatarException;
import com.prgrms.ijuju.domain.avatar.exception.ItemErrorCode;
import com.prgrms.ijuju.domain.avatar.exception.ItemException;
import com.prgrms.ijuju.domain.member.dto.response.ErrorMemberResponse;
import com.prgrms.ijuju.domain.member.exception.MemberErrorCode;
import com.prgrms.ijuju.domain.member.exception.MemberException;
import com.prgrms.ijuju.domain.minigame.flipcard.dto.response.ErrorFlipCardResponse;
import com.prgrms.ijuju.domain.minigame.flipcard.exception.FlipCardErrorCode;
import com.prgrms.ijuju.domain.minigame.flipcard.exception.FlipCardException;
import com.prgrms.ijuju.domain.minigame.wordquiz.dto.response.ErrorWordQuizResponse;
import com.prgrms.ijuju.domain.minigame.wordquiz.exception.WordQuizErrorCode;
import com.prgrms.ijuju.domain.minigame.wordquiz.exception.WordQuizException;
import com.prgrms.ijuju.domain.stock.mid.dto.response.ErrorMidResponse;
import com.prgrms.ijuju.domain.stock.mid.exception.MidStockErrorCode;
import com.prgrms.ijuju.domain.stock.mid.exception.MidStockException;
import com.prgrms.ijuju.global.common.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

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

    @ExceptionHandler(MidStockException.class)
    protected ResponseEntity<ErrorMidResponse> handleMidStockException(final MidStockException e) {
        log.error("MidStockException: {}", e.getMessage());
        final MidStockErrorCode errorCode = e.getErrorCode();
        final ErrorMidResponse response = ErrorMidResponse.of(errorCode);
        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

    @ExceptionHandler(FlipCardException.class)
    protected ResponseEntity<ErrorFlipCardResponse> handleFlipCardException(final FlipCardException e) {
        log.error("FlipCardException: {}", e.getMessage());
        final FlipCardErrorCode errorCode = e.getErrorCode();
        final ErrorFlipCardResponse response = ErrorFlipCardResponse.of(errorCode);
        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

    @ExceptionHandler(WordQuizException.class)
    protected ResponseEntity<ErrorWordQuizResponse> handleWordQuizException(final WordQuizException e) {
        log.error("WordQuizException: {}", e.getMessage());
        final WordQuizErrorCode errorCode = e.getErrorCode();
        final ErrorWordQuizResponse response = ErrorWordQuizResponse.of(errorCode);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(MemberException.class)
    protected ResponseEntity<ErrorMemberResponse> handleMemberException(final MemberException e) {
        log.error("MemberException: {}", e.getMessage());
        final MemberErrorCode errorCode = e.getErrorCode();
        final ErrorMemberResponse response = ErrorMemberResponse.of(errorCode);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(ItemException.class)
    protected ResponseEntity<ErrorItemResponse> handleItemException(final ItemException e) {
        log.error("ItemException: {}", e.getMessage());
        final ItemErrorCode errorCode = e.getErrorCode();
        final ErrorItemResponse response = ErrorItemResponse.of(errorCode);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(AvatarException.class)
    protected ResponseEntity<ErrorAvatarResponse> handleAvatarException(final AvatarException e) {
        log.error("AvatarException: {}", e.getMessage());
        final AvatarErrorCode errorCode = e.getErrorCode();
        final ErrorAvatarResponse response = ErrorAvatarResponse.of(errorCode);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
