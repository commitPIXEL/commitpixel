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
    private String githubImage;
    private String date;
    private Integer value;

    @Builder
    public FlourishDto(Integer providerId, String url, String githubNickname,
        String githubImage, String date, Integer value) {
        this.providerId = providerId;
        this.url = url;
        this.githubNickname = githubNickname;
        this.githubImage = githubImage;
        this.date = date;
        this.value = value;
    }


}
