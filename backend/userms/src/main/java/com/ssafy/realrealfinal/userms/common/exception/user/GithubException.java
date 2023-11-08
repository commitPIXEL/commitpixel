package com.ssafy.realrealfinal.userms.common.exception.user;

import com.ssafy.realrealfinal.userms.common.model.BaseException;
import com.ssafy.realrealfinal.userms.common.exception.ErrorCode;

public class GithubException extends BaseException {
    public GithubException() {super(ErrorCode.GITHUB_IO_FAILED);}
}


