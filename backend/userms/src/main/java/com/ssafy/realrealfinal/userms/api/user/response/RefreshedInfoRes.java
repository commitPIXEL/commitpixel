package com.ssafy.realrealfinal.userms.api.user.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RefreshedInfoRes {

    private Integer totalCredit;
    private Integer availablePixel;
    private String githubNickname;

    @Builder
    public RefreshedInfoRes(Integer totalCredit, Integer availablePixel, String githubNickname) {
        this.totalCredit = totalCredit;
        this.availablePixel = availablePixel;
        this.githubNickname = githubNickname;
    }
}
