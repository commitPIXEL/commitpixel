package com.ssafy.realrealfinal.userms.common.exception.user;

import com.ssafy.realrealfinal.userms.common.exception.ErrorCode;
import com.ssafy.realrealfinal.userms.common.model.BaseException;

public class WhitelistNotFoundException extends BaseException {

    public WhitelistNotFoundException() {
        super(ErrorCode.WHITELIST_NOT_FOUND);
    }
}
