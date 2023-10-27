package com.ssafy.realrealfinal.authms.common.exception.auth;

import com.ssafy.realrealfinal.authms.common.model.BaseException;
import com.ssafy.realrealfinal.authms.common.exception.ErrorCode;

public class GithubException extends BaseException {
    public GithubException() {super(ErrorCode.GITHUB_IO_FAILED);}
}
