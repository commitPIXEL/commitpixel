package com.ssafy.realrealfinal.imagems.common.exception.image;

import com.ssafy.realrealfinal.imagems.common.exception.ErrorCode;
import com.ssafy.realrealfinal.imagems.common.model.BaseException;

public class ImageConvertException extends BaseException {
    public ImageConvertException() {
        super(ErrorCode.IMAGE_CONVERT_IO_ERROR);
    }
}
