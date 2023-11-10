package com.ssafy.realrealfinal.rankms.api.rank.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserRankDto {
    private String githubNickname;
    private Integer pixelNum;

    @Builder
    public UserRankDto(String githubNickname, Integer pixelNum) {
        this.githubNickname = githubNickname;
        this.pixelNum = pixelNum;
    }
}
