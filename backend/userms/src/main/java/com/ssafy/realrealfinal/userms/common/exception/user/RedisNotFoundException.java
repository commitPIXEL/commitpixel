package com.ssafy.realrealfinal.userms.common.exception.user;

import com.ssafy.realrealfinal.userms.common.exception.ErrorCode;
import com.ssafy.realrealfinal.userms.common.model.BaseException;

public class RedisNotFoundException extends BaseException {
    public RedisNotFoundException() {
        super(ErrorCode.REDIS_NOT_FOUND);
    }
}
