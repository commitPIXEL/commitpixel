package com.ssafy.realrealfinal.imagems.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    UNEXPECTED_ERROR(HttpStatus.BAD_REQUEST, "0"); // 예시

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
