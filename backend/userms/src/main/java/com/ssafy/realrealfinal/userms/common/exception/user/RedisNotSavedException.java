package com.ssafy.realrealfinal.userms.common.exception.user;

import com.ssafy.realrealfinal.userms.common.exception.ErrorCode;
import com.ssafy.realrealfinal.userms.common.model.BaseException;

public class RedisNotSavedException extends BaseException {

    public RedisNotSavedException() {super(ErrorCode.REDIS_NOT_SAVED);}
}

