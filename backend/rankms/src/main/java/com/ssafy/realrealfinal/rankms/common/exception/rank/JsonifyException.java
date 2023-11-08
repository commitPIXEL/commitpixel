package com.ssafy.realrealfinal.rankms.common.exception.rank;

import com.ssafy.realrealfinal.rankms.common.exception.ErrorCode;
import com.ssafy.realrealfinal.rankms.common.model.BaseException;

public class JsonifyException extends BaseException {
    public JsonifyException() {super(ErrorCode.JSON_FAILED);}

}
