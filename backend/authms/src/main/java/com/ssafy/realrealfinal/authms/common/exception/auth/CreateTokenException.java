package com.ssafy.realrealfinal.authms.common.exception.auth;

import com.ssafy.realrealfinal.authms.common.exception.ErrorCode;
import com.ssafy.realrealfinal.authms.common.model.BaseException;

public class CreateTokenException extends BaseException {

    public CreateTokenException() {
        super(ErrorCode.TOKEN_CREATE_FAILED);
    }

}

