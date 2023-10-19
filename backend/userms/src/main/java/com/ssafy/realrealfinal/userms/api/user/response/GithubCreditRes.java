package com.ssafy.realrealfinal.userms.api.user.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GithubCreditRes {

    private Integer usedPixel;
    private Integer availablePixel;

    @Builder
    public GithubCreditRes(Integer usedPixel, Integer availablePixel) {
        this.usedPixel = usedPixel;
        this.availablePixel = availablePixel;
    }
}
