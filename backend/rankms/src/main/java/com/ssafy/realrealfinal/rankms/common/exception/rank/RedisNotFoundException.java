package com.ssafy.realrealfinal.rankms.common.exception.rank;


import com.ssafy.realrealfinal.rankms.common.exception.ErrorCode;
import com.ssafy.realrealfinal.rankms.common.model.BaseException;

public class RedisNotFoundException extends BaseException {
    public RedisNotFoundException() {super(ErrorCode.REDIS_NOT_FOUND);}
}
