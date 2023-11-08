package com.ssafy.realrealfinal.pixelms.common.model.pixel;

import com.ssafy.realrealfinal.pixelms.common.exception.ErrorCode;
import com.ssafy.realrealfinal.pixelms.common.model.BaseException;

public class RedisNotFoundException extends BaseException {
    public RedisNotFoundException() {
        super(ErrorCode.REDIS_NOT_FOUND);
    }
}
