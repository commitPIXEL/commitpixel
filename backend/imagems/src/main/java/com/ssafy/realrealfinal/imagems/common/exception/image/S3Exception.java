package com.ssafy.realrealfinal.imagems.common.exception.image;

import com.ssafy.realrealfinal.imagems.common.exception.ErrorCode;
import com.ssafy.realrealfinal.imagems.common.model.BaseException;

public class S3Exception extends BaseException {

    public S3Exception() {
        super(ErrorCode.S3_IO_FAILED);
    }
}
