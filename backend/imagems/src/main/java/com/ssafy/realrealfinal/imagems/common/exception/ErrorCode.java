package com.ssafy.realrealfinal.imagems.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    UNEXPECTED_ERROR("커스텀 에러 메시지", HttpStatus.BAD_REQUEST), // 예시

    IMAGE_CONVERT_IO_ERROR("FAIL TO CONVERT FILE", HttpStatus.BAD_REQUEST),
    GIF_CONVERT_FAILED("FAIL TO CONVERT IMAGES TO GIF", HttpStatus.CONFLICT),
    S3_IO_FAILED("FAILED WHILE ACCESSING TO S3", HttpStatus.CONFLICT);

    private final String message;
    private final HttpStatus httpStatus;
}
