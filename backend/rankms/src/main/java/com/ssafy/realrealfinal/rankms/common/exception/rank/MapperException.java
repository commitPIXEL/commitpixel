package com.ssafy.realrealfinal.rankms.common.exception.rank;

import com.ssafy.realrealfinal.rankms.common.exception.ErrorCode;
import com.ssafy.realrealfinal.rankms.common.model.BaseException;

public class MapperException extends BaseException {

    public MapperException() {
        super(ErrorCode.MAPPING_FAILED);
    }


}
