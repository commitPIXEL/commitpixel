package com.ssafy.realrealfinal.authms.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    UNEXPECTED_ERROR("예상되지 않은 에러", HttpStatus.BAD_REQUEST); // 예시
    GITHUB_IO_FAILED(HttpStatus.BAD_REQUEST, "999"),
    REDIS_NOT_SAVED(HttpStatus.SERVICE_UNAVAILABLE, "FAILED TO SAVE TOKEN IN REDIS"),
    REDIS_NOT_DELETED(HttpStatus.SERVICE_UNAVAILABLE, "FAILED TO DELETE TOKEN IN REDIS");
    //GITHUB에서 인가코드로 토큰 발급 받을 때 io exception발생.

    private final HttpStatus httpStatus;
    private final String message;
    private final HttpStatus httpStatus;
}
