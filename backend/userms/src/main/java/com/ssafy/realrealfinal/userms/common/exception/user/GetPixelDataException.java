package com.ssafy.realrealfinal.userms.common.exception.user;

import com.ssafy.realrealfinal.userms.common.exception.ErrorCode;
import com.ssafy.realrealfinal.userms.common.model.BaseException;

public class GetPixelDataException extends BaseException {
    public GetPixelDataException() {super(ErrorCode.PIXEL_REDIS_FAILED);}
}
