package com.ssafy.realrealfinal.authms.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    UNEXPECTED_ERROR("예상되지 않은 에러", HttpStatus.BAD_REQUEST), // 예시

    REDIS_NOT_SAVED("FAILED TO SAVE TOKEN IN REDIS", HttpStatus.SERVICE_UNAVAILABLE),
    REDIS_NOT_DELETED("FAILED TO DELETE TOKEN IN REDIS", HttpStatus.SERVICE_UNAVAILABLE),
    GITHUB_IO_FAILED("FAILED WHILE REQUESTING TO GITHUB", HttpStatus.CONFLICT),
    TOKEN_CREATE_FAILED("FAILED TO CREATE JWT TOKEN", HttpStatus.CONFLICT),
    TOKEN_NOT_VALID("TOKEN IS NOT VALID", HttpStatus.BAD_REQUEST),
    MAPPING_PROBLEM("FAILED WHILE MAPPING. BACKEND PROBLEM", HttpStatus.CONFLICT);
    //GITHUB에서 인가코드로 토큰 발급 받을 때 io exception발생.

    private final String message;
    private final HttpStatus httpStatus;
}
