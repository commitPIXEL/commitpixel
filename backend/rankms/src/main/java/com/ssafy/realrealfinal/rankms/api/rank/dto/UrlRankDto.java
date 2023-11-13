package com.ssafy.realrealfinal.rankms.api.rank.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UrlRankDto {
    private String url;
    private Integer pixelNum;

    @Builder
    public UrlRankDto(String url, Integer pixelNum) {
        this.url = url;
        this.pixelNum = pixelNum;
    }
}
