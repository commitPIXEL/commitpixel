package com.ssafy.realrealfinal.userms.api.user.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserInfoRes {

    private String githubNickname;
    private String profileImage;
    private Integer totalPixel;
    private Integer availablePixel;
    private String url;

    @Builder
    public UserInfoRes(String githubNickname, String profileImage, Integer totalPixel,
        Integer availablePixel, String url) {
        this.githubNickname = githubNickname;
        this.profileImage = profileImage;
        this.totalPixel = totalPixel;
        this.availablePixel = availablePixel;
        this.url = url;
    }
}
