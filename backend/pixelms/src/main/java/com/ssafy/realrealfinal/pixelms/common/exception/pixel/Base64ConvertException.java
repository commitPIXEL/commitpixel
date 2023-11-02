package com.ssafy.realrealfinal.pixelms.common.exception.pixel;

import com.ssafy.realrealfinal.pixelms.common.model.BaseException;
import com.ssafy.realrealfinal.pixelms.common.exception.ErrorCode;

public class Base64ConvertException extends
    BaseException {
    public     Base64ConvertException() {super(ErrorCode.BASE64_CONVERT_FAILED);}

}
