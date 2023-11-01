package com.ssafy.realrealfinal.rankms.api.rank.dto;

import lombok.Getter;

@Getter
public class UpdatePixelDto {
    private String oldGithubNickname;
    private String oldUrl;
    private String newGithubNickname;
    private String newUrl;
}
