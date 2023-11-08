package com.ssafy.realrealfinal.rankms.common.exception.rank;


import com.ssafy.realrealfinal.rankms.common.model.BaseException;
import com.ssafy.realrealfinal.rankms.common.exception.ErrorCode;

public class JsonifyException extends BaseException {
    public JsonifyException() {super(ErrorCode.JSON_FAILED);}

}

