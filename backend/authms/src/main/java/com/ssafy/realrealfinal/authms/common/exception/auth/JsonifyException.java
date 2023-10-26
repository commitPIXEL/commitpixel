package com.ssafy.realrealfinal.authms.common.exception.auth;

import com.ssafy.realrealfinal.authms.common.exception.ErrorCode;
import com.ssafy.realrealfinal.authms.common.model.BaseException;

public class JsonifyException extends BaseException {
    public JsonifyException() {super(ErrorCode.JSON_FAILED);}

}
