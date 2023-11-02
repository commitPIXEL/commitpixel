package com.ssafy.realrealfinal.pixelms.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    REDIS_NOT_FOUND("NO SUCH DATA IN REDIS", HttpStatus.NOT_FOUND),
    BASE64_CONVERT_FAILED("FAILED WHILE CONVERTING BUFFEREDIMAGE TO BASE64 IMAGE IO",
        HttpStatus.CONFLICT);

    private final String message;
    private final HttpStatus httpStatus;
}
