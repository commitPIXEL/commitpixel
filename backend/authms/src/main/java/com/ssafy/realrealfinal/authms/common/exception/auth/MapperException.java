package com.ssafy.realrealfinal.authms.common.exception.auth;

import com.ssafy.realrealfinal.authms.common.model.BaseException;
import com.ssafy.realrealfinal.authms.common.exception.ErrorCode;

public class MapperException extends BaseException {
    public MapperException() {super(ErrorCode.MAPPING_PROBLEM);}


}
