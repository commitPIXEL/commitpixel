package com.ssafy.realrealfinal.authms.common.exception.auth;

import com.ssafy.realrealfinal.authms.common.exception.ErrorCode;
import com.ssafy.realrealfinal.authms.common.model.BaseException;

public class RedisNotSavedException extends BaseException {
    public RedisNotSavedException() {super(ErrorCode.REDIS_NOT_SAVED);}
}
