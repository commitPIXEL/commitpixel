package com.ssafy.realrealfinal.authms.common.exception.auth;
import com.ssafy.realrealfinal.authms.common.model.BaseException;
import com.ssafy.realrealfinal.authms.common.exception.ErrorCode;
public class ExpiredTokenException extends BaseException {
    public ExpiredTokenException() {super(ErrorCode.TOKEN_NOT_VALID);}


}
