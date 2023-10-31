package com.ssafy.realrealfinal.imagems.common.exception.image;

import com.ssafy.realrealfinal.imagems.common.exception.ErrorCode;
import com.ssafy.realrealfinal.imagems.common.model.BaseException;

public class GifConvertException extends BaseException {

    public GifConvertException() {
        super(ErrorCode.GIF_CONVERT_FAILED);
    }


}
