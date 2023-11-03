package com.ssafy.realrealfinal.userms.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    JSON_FAILED("FAILED TO CONVERT JSON TO VAR", HttpStatus.CONFLICT),
    SOLVEDAC_AUTH_FAILED("FAILED TO AUTHORIZE SOLVEDAC", HttpStatus.BAD_REQUEST),
    API_REQUEST_FAILED("FAILED WHILE REQUESTING API REQUEST (BACKEND)", HttpStatus.CONFLICT),
    REDIS_NOT_SAVED("FAILED DURING SAVING IN REDIS (BACKEND)", HttpStatus.CONFLICT),
    REDIS_NOT_FOUND("FAIL TO FIND DATA IN REDIS", HttpStatus.NOT_FOUND),
    PIXEL_REDIS_FAILED("FAILED TO GET PIXEL DATA FROM REDIS", HttpStatus.CONFLICT),
    GITHUB_IO_FAILED("FAILED WHILE REQUESTING TO GITHUB", HttpStatus.CONFLICT),
    WHITELIST_NOT_SAVED("FAILED DURING SAVING IN MYSQL", HttpStatus.CONFLICT);

    private final String message;
    private final HttpStatus httpStatus;
}
