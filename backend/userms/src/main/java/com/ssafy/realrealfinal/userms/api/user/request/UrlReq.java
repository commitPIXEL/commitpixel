package com.ssafy.realrealfinal.userms.api.user.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class UrlReq {

    private String url;

    @Builder
    public UrlReq(String url) {
        this.url = url;
    }
}
