package com.ssafy.realrealfinal.userms.common.exception.user;

import com.ssafy.realrealfinal.userms.common.exception.ErrorCode;
import com.ssafy.realrealfinal.userms.common.model.BaseException;

public class SolvedAcAuthException extends BaseException {
    public SolvedAcAuthException() {super(ErrorCode.SOLVEDAC_AUTH_FAILED);}

}
