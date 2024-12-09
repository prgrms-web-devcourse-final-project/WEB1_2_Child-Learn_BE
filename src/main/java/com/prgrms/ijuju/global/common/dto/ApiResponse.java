package com.prgrms.ijuju.global.common.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {
    private String code;
    private String message;
    private T data;
    private int statusCode;
    // private HttpStatus status;

    @Builder
    private ApiResponse(String code, String message, T data, int statusCode) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.statusCode = statusCode;
    }

    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .message(message)
                .build();   
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(String code, String message, int statusCode) {
        return ApiResponse.<T>builder()
                .code(code)
                .message(message)
                .statusCode(statusCode)
                .build();
    }
} 