package com.ssafy.realrealfinal.authms.common.exception.auth;
import com.ssafy.realrealfinal.authms.common.model.BaseException;
import com.ssafy.realrealfinal.authms.common.exception.ErrorCode;
public class UnexpectedTokenException extends BaseException {

    public UnexpectedTokenException() {super(ErrorCode.UNEXPECTED_ERROR);}

}
