package com.ssafy.realrealfinal.rankms.api.rank.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class UserInfoDto {

    private Integer providerId;
    private String url;
    private String profileImage;

    @Builder
    public UserInfoDto(Integer providerId, String url, String profileImage) {
        this.providerId = providerId;
        this.url = url;
        this.profileImage = profileImage;
    }
}
