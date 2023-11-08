package com.ssafy.realrealfinal.userms.api.user.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserInfoRes {

    private String githubNickname;
    private String profileImage;
    private String url;
    private Boolean isSolvedACAuth;

    @Builder
    public UserInfoRes(String githubNickname, String profileImage, String url,
        Boolean isSolvedACAuth) {
        this.githubNickname = githubNickname;
        this.profileImage = profileImage;
        this.url = url;
        this.isSolvedACAuth = isSolvedACAuth;
    }
}
