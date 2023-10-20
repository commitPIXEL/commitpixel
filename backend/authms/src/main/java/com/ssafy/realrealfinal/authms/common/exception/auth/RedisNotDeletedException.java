package com.ssafy.realrealfinal.authms.common.exception.auth;

import com.ssafy.realrealfinal.authms.common.exception.ErrorCode;
import com.ssafy.realrealfinal.authms.common.model.BaseException;

public class RedisNotDeletedException extends BaseException {
    public RedisNotDeletedException() {super(ErrorCode.REDIS_NOT_DELETED);}

}
