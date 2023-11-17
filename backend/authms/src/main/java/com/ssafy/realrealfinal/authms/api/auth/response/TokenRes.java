package com.ssafy.realrealfinal.authms.api.auth.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TokenRes {

    private final String jwtAccessToken;
    private final String jwtRefreshToken;
    private String nickname;

    @Builder
    public TokenRes(String jwtAccessToken, String jwtRefreshToken, String nickname) {
        this.jwtAccessToken = jwtAccessToken;
        this.jwtRefreshToken = jwtRefreshToken;
        this.nickname = nickname;
    }
}
