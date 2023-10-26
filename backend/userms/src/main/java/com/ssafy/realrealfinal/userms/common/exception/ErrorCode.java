package com.ssafy.realrealfinal.userms.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    UNEXPECTED_ERROR("0", HttpStatus.BAD_REQUEST),
    JSON_FAILED("FAILED TO CONVERT JSON TO VAR", HttpStatus.CONFLICT);

    private final String message;
    private final HttpStatus httpStatus;
}
