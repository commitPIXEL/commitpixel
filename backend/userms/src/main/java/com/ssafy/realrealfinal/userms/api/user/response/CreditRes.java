package com.ssafy.realrealfinal.userms.api.user.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CreditRes {

    private Integer totalPixel;
    private Integer availablePixel;

    @Builder
    public CreditRes(Integer totalPixel, Integer availablePixel) {
        this.totalPixel = totalPixel;
        this.availablePixel = availablePixel;
    }
}
