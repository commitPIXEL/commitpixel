package com.ssafy.realrealfinal.authms.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    UNEXPECTED_ERROR("예상되지 않은 에러", HttpStatus.BAD_REQUEST); // 예시

    private final String message;
    private final HttpStatus httpStatus;
}
