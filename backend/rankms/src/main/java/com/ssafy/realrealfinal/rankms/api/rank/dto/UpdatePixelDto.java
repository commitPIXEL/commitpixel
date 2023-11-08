package com.ssafy.realrealfinal.rankms.api.rank.dto;

import lombok.Getter;

@Getter
public class UpdatePixelDto {
    private String prevGithubNickname;
    private String prevUrl;
    private String currGithubNickname;
    private String currUrl;
}
