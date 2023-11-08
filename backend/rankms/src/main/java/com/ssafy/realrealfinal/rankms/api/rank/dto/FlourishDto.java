package com.ssafy.realrealfinal.rankms.api.rank.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FlourishDto {

    private Integer providerId;
    private String url;
    private String githubNickname;
    private String profileImage;
    private String date;
    private Integer value;

    @Builder
    public FlourishDto(Integer providerId, String url, String githubNickname,
        String profileImage, String date, Integer value) {
        this.providerId = providerId;
        this.url = url;
        this.githubNickname = githubNickname;
        this.profileImage = profileImage;
        this.date = date;
        this.value = value;
    }


}
