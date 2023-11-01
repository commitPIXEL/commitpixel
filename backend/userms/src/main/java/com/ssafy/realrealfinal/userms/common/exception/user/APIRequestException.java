package com.ssafy.realrealfinal.userms.common.exception.user;

import com.ssafy.realrealfinal.userms.common.exception.ErrorCode;
import com.ssafy.realrealfinal.userms.common.model.BaseException;

public class APIRequestException extends BaseException {

    public APIRequestException() {
        super(ErrorCode.API_REQUEST_FAILED);
    }
}

