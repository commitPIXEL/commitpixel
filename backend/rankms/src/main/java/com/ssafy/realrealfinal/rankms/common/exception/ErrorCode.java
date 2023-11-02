package com.ssafy.realrealfinal.rankms.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    UNEXPECTED_ERROR("커스텀 에러 메시지", HttpStatus.BAD_REQUEST), // 예시
    JSON_FAILED("FAILED TO CONVERT JSON TO VAR", HttpStatus.CONFLICT),
    REDIS_NOT_FOUND("랭크 레디스에서 찾지 못했습니다ㅠ", HttpStatus.NOT_FOUND);


    private final String message;
    private final HttpStatus httpStatus;
}
