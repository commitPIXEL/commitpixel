package com.ssafy.realrealfinal.rankms.common.exception.rank;


import com.ssafy.realrealfinal.rankms.common.model.BaseException;
import com.ssafy.realrealfinal.rankms.common.exception.ErrorCode;
public class RedisNotFoundException extends BaseException {
    public RedisNotFoundException() {super(ErrorCode.REDIS_NOT_FOUND);}
}

