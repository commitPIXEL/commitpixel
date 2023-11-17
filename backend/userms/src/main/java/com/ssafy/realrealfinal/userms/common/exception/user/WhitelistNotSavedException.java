package com.ssafy.realrealfinal.userms.common.exception.user;

import com.ssafy.realrealfinal.userms.common.exception.ErrorCode;
import com.ssafy.realrealfinal.userms.common.model.BaseException;

public class WhitelistNotSavedException extends BaseException {

    public WhitelistNotSavedException() {
        super(ErrorCode.WHITELIST_NOT_SAVED);
    }
}
