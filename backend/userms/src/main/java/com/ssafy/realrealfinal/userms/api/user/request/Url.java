package com.ssafy.realrealfinal.userms.api.user.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Url {

    private String url;

    @Builder
    public Url(String url) {
        this.url = url;
    }
}
