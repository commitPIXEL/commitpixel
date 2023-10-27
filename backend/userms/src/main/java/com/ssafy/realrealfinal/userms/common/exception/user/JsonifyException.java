package com.ssafy.realrealfinal.userms.common.exception.user;

import com.ssafy.realrealfinal.userms.common.model.BaseException;
import com.ssafy.realrealfinal.userms.common.exception.ErrorCode;

public class JsonifyException extends BaseException {
    public JsonifyException() {super(ErrorCode.JSON_FAILED);}

}
