package com.ssafy.realrealfinal.pixelms.common.exception.pixel;

import com.ssafy.realrealfinal.pixelms.common.exception.ErrorCode;
import com.ssafy.realrealfinal.pixelms.common.model.BaseException;

public class JsonifyException extends BaseException {
    public JsonifyException() {super(ErrorCode.JSON_FAILED);}
}
