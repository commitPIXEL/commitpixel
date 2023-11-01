package com.ssafy.realrealfinal.userms.api.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RefreshInfoDto {

    private Integer refreshedCredit;
    private String githubNickname;

    @Builder
    public RefreshInfoDto(Integer refreshedCredit, String githubNickname) {
        this.refreshedCredit = refreshedCredit;
        this.githubNickname = githubNickname;
    }
}
