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

    @Builder
    public UserInfoRes(String githubNickname, String profileImage, String url) {
        this.githubNickname = githubNickname;
        this.profileImage = profileImage;
        this.url = url;
    }
}
