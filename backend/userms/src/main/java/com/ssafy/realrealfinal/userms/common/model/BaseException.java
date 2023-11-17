package com.ssafy.realrealfinal.userms.common.model;

import com.ssafy.realrealfinal.userms.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final ErrorCode errorCode;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /**
     * Errorcode 외에 별도의 메시지가 필요할 경우 추가하여 작성 가능
     */
    public BaseException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}