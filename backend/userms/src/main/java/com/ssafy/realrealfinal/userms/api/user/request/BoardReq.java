package com.ssafy.realrealfinal.userms.api.user.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BoardReq {

    private Integer type;
    private String content;

    @Builder
    public BoardReq(Integer type, String content) {
        this.type = type;
        this.content = content;
    }
}
